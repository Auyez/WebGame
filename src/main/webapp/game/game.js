class Game {
    constructor(parent) {
    	
        this.game = new Phaser.Game(
                        800, 600, Phaser.AUTO, parent,
                            {
                            preload: this.preload, create: this.create, update: this.update
                            }
                        );
        this.players = {}; // id -> player
        this.cursors = null;
        
    }


    preload() {
        this.game.load.image('dude', 'game/assets/bitman.png');
        this.game.load.image('tile', 'game/assets/map.png');
    }
    
    create() {
    	var ready = new DataView(new ArrayBuffer(4));
        ready.setInt8(0, 0); // sends 0 as lobby index for now
		ready.setInt8(1, Protocol.Server.GAME_MSG);
		ready.setInt8(2, Protocol.Server.Game.READY);
		socket.send(ready) // Sends to server that game is loaded and ready to receive PLAYER_SETUP
		
		
        this.cursors = this.game.input.keyboard.createCursorKeys();
		this.q = this.game.input.keyboard.addKey(Phaser.Keyboard.Q); //.onDown .onPress
		//this.q.onDown.add(spellOne, )
		
        this.message = new DataView(new ArrayBuffer(4));
		this.message.setInt8(0, 0); // sends 0 as lobby index for now
		this.message.setInt8(1, Protocol.Server.GAME_MSG);
		this.message.setInt8(2, Protocol.Server.Game.INPUT);
		this.socket = socket; // Unnecessary?
		this.message.setInt8(2, Protocol.Server.Game.INPUT);
		
		
		///////////////////////
    	var map = [
			[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1], 
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
			[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1]
    	];
    	
    	for (var i = 0; i < 30; i++){
    		for(var j = 0; j < 40; j++){
    			if(map[i][j] == 1){
    				var test = this.add.sprite( (j % 40) * 20, (i % 30) * 20, 'tile' );
    			}
    		}
    	}
    	//////////////////////////////////////////////////
    }

    update() {
    	if (this.cursors.up.isDown){
        	this.message.setInt8(3, 'w'.charCodeAt(0));
        	this.socket.send(this.message);
        }
    	if (this.cursors.down.isDown){
        	this.message.setInt8(3, 's'.charCodeAt(0));
        	this.socket.send(this.message);
        }
    	if(this.cursors.left.isDown){
        	this.message.setInt8(3, 'a'.charCodeAt(0));
        	this.socket.send(this.message);
        }
    	if(this.cursors.right.isDown){
        	this.message.setInt8(3, 'd'.charCodeAt(0));
        	this.socket.send(this.message);
        } 
    	
    }

    onmessage(arrayBuf) {
        //console.log("Game: ", new Int8Array(arrayBuf));
        var dataView = new DataView(arrayBuf);
        //console.log("Game cmd: ", Protocol.Client.Game.getGameCmd(dataView) );

        var command = Protocol.Client.Game.getGameCmd(dataView);
        if (command == Protocol.Client.Game.WORLD_STATE) {
            var worldState = Protocol.Client.Game.getWorldState(dataView);
            this.updateEntities(worldState);
        }

        if (command == Protocol.Client.Game.PLAYER_SETUP) {
        	//console.log(arrayBuf);
        	console.log("Game: ", new Uint8Array(arrayBuf));
            var ids = Protocol.Client.Game.getPlayerSetup(dataView);
            this.addPlayers(ids);
        }
    }

    addPlayers(ids) {
        for (var i in ids) {
            var id = ids[i];
            var player = this.game.add.sprite(32, this.game.world.height - 150, 'dude');
            //player.animations.add('left', [0, 1, 2, 3], 10, true);
            //player.animations.add('right', [5, 6, 7, 8], 10, true);
            this.players[id] = player;
        }
    }

    updateEntities(worldState) {
        for (var i in worldState) {
            var entity = worldState[i];
            if (entity.type == Protocol.Client.Game.Entity.PLAYER) {
                if (entity.id in this.players) {
                    var player = this.players[entity.id];
                    player.x = entity.x;
                    player.y = entity.y;
                }
            }
        }
    }
}
