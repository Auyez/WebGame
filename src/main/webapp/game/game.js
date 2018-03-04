function CreateGame(parent, socket, lobbyIndex) {
	var game = new Phaser.Game(
			            800, 600, Phaser.AUTO, parent,
			            {
			            	preload: preload, 
			            	create: create, 
			            	update: update
			            }
		        	);
	
	var players = {}; // id -> player
    var cursors = null;
	var q = null;
	var inputMessage = null;
    
    function preload() {
        game.load.image('dude', 'game/assets/bitman.png');
        game.load.image('tile', 'game/assets/map.png');
    }
    
    function create() {
    	var ready = new DataView(new ArrayBuffer(4));
        ready.setInt8(0, lobbyIndex);
		ready.setInt8(1, Protocol.Server.GAME_MSG);
		ready.setInt8(2, Protocol.Server.Game.READY);
		socket.send(ready) // Sends to server that game is loaded and ready to receive PLAYER_SETUP
		
		
        cursors = game.input.keyboard.createCursorKeys();
		q = game.input.keyboard.addKey(Phaser.Keyboard.Q); //.onDown .onPress
		//q.onDown.add(spellOne, )
		
        inputMessage = new DataView(new ArrayBuffer(4));
		inputMessage.setInt8(0, lobbyIndex);
		inputMessage.setInt8(1, Protocol.Server.GAME_MSG);
		inputMessage.setInt8(2, Protocol.Server.Game.INPUT);
		
		
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
    				var test = game.add.sprite( (j % 40) * 20, (i % 30) * 20, 'tile' );
    			}
    		}
    	}
    	//////////////////////////////////////////////////
    }

    function update() {
    	if (cursors.up.isDown){
        	inputMessage.setInt8(3, 'w'.charCodeAt(0));
        	socket.send(inputMessage);
        }
    	if (cursors.down.isDown){
        	inputMessage.setInt8(3, 's'.charCodeAt(0));
        	socket.send(inputMessage);
        }
    	if(cursors.left.isDown){
        	inputMessage.setInt8(3, 'a'.charCodeAt(0));
        	socket.send(inputMessage);
        }
    	if(cursors.right.isDown){
        	inputMessage.setInt8(3, 'd'.charCodeAt(0));
        	socket.send(inputMessage);
        } 
    	
    }

    game.onmessage = function(arrayBuf) {
        //console.log("Game: ", new Int8Array(arrayBuf));
        var dataView = new DataView(arrayBuf);
        //console.log("Game cmd: ", Protocol.Client.Game.getGameCmd(dataView) );

        var command = Protocol.Client.Game.getGameCmd(dataView);
        if (command == Protocol.Client.Game.WORLD_STATE) {
            var worldState = Protocol.Client.Game.getWorldState(dataView);
            updateEntities(worldState);
        }

        if (command == Protocol.Client.Game.PLAYER_SETUP) {
        	//console.log(arrayBuf);
        	console.log("Game: ", new Uint8Array(arrayBuf));
            var ids = Protocol.Client.Game.getPlayerSetup(dataView);
            addPlayers(ids);
        }
    }

    function addPlayers(ids) {
        for (var i in ids) {
            var id = ids[i];
            var player = game.add.sprite(32, game.world.height - 150, 'dude');
            //player.animations.add('left', [0, 1, 2, 3], 10, true);
            //player.animations.add('right', [5, 6, 7, 8], 10, true);
            players[id] = player;
        }
    }

    function updateEntities(worldState) {
        for (var i in worldState) {
            var entity = worldState[i];
            if (entity.type == Protocol.Client.Game.Entity.PLAYER) {
                if (entity.id in players) {
                    var player = players[entity.id];
                    player.x = entity.x;
                    player.y = entity.y;
                }
            }
        }
    }	
	
	return game;
}


class Game2 {
    constructor(parent, socket, lobbyIndex) {
    	
    	// this in game.preload would refer to game
        this.game = new Phaser.Game(
                        800, 600, Phaser.AUTO, parent,
                            {
                            preload: function(){this.preload();},
                            create: function(){this.create();}, 
                            update: function(){this.update();}
                            }
                        );
        this.players = {}; // id -> player
        this.cursors = null;
        this.socket = socket;
        this.lobbyIndex = lobbyIndex;
        
    }



}
