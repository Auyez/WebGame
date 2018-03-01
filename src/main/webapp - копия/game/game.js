class Game {
    constructor(parent, lobbyIndex) {
        this.game = new Phaser.Game(
                        800, 800, Phaser.AUTO, parent,
                            {
                            preload: this.preload, create: this.create, update: this.update
                            }
                        );
        this.player = null;
        this.cursors = null;       
        this.lobbyIndex = lobbyIndex;

    }


    preload() {
        this.game.load.spritesheet('dude', 'game/assets/dude.png', 32, 48);
    }


    create() {
		this.cursors = this.game.input.keyboard.createCursorKeys();
		this.message = new DataView(new ArrayBuffer(7));
		this.message.setInt8(0, this.lobbyIndex);
		this.message.setInt8(1, Protocol.Server.GAME_MSG);
		this.message.setInt32(2, 100500);
        this.socket = new WebSocket("ws://" + location.host + "/WebGame/websocketendpoint");
    }

    update() {
        if (this.cursors.up.isDown){
        	this.message.setInt8(6, 'w'.charCodeAt(0));
        	this.socket.send(this.message);
        }
    }

    onmessage(arrayBuf) {
        console.log("Game: ", new Int8Array(arrayBuf));
        
    }
}
