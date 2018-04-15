var FRAME_WIDTH = 32;
var FRAME_HEIGHT = 36;

function CreateGame(parent, socket, lobbyIndex) {
	var game = new Phaser.Game(
			            1200, 900, Phaser.AUTO, parent,
			            {
			            	preload: preload, 
			            	create: create, 
			            	update: update
			            }
		        	);
	var actorManager = new ActorManager(game);
    var cursors;
	var q,w,e,r;
	var inputMessage = null;
	var ready = false;

    function preload() {
        game.load.image('tile', 'game/assets/map.png');
        ActorManager.preload(game);
    }
    
    function create() {
        var servermsg = new Protocol.Server.ServerMsg();
		servermsg.lobbyIndex = lobbyIndex;
		servermsg.lobbyCmd = new Protocol.Server.LobbyCmd();
		servermsg.lobbyCmd.ready = new Protocol.Server.Ready();
		// Sends to server that game is loaded. Not really used anymore
		socket.send(servermsg.bytes());

        inputMessage = new Protocol.Server.ServerMsg();
        inputMessage.lobbyIndex = lobbyIndex;
        inputMessage.lobbyCmd = new Protocol.Server.LobbyCmd();
        inputMessage.lobbyCmd.gameMsg = new Protocol.Server.GameMsg();
        // inputMessage.lobbyCmd.gameMsg.input = new Protocol.Server.Input();
        

        cursors = game.input.keyboard.createCursorKeys();
		q = game.input.keyboard.addKey(Phaser.Keyboard.Q);
		w = game.input.keyboard.addKey(Phaser.Keyboard.W);
		e = game.input.keyboard.addKey(Phaser.Keyboard.E);
		r = game.input.keyboard.addKey(Phaser.Keyboard.R);
		q.onDown.add(function(){spell(0)});
		w.onDown.add(function(){spell(1)});
		e.onDown.add(function(){spell(2)});
		r.onDown.add(function(){spell(3)});
    	game.input.onDown.add(move, this);
    	
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
    				var test = game.add.sprite( (j % 40) * 30, (i % 30) * 30, 'tile' );
    			}
    		}
    	}
    	//////////////////////////////////////////////////

    	ready = true;
    }

    function update() {
    	actorManager.update();
    }
    
    function spell(item) {
    	inputMessage.lobbyCmd.gameMsg.input = null;
    	inputMessage.lobbyCmd.gameMsg.skillInput = new Protocol.Server.SkillInput();
    	inputMessage.lobbyCmd.gameMsg.skillInput.x = game.input.x;
    	inputMessage.lobbyCmd.gameMsg.skillInput.y = game.input.y;
    	inputMessage.lobbyCmd.gameMsg.skillInput.skillType = item;
    	socket.send(inputMessage.bytes());
    }
    
    function move() {
    	inputMessage.lobbyCmd.gameMsg.skillInput = null;
    	inputMessage.lobbyCmd.gameMsg.input = new Protocol.Server.Input();
    	inputMessage.lobbyCmd.gameMsg.input.xTarget = game.input.x;
    	inputMessage.lobbyCmd.gameMsg.input.yTarget = game.input.y;
    	socket.send(inputMessage.bytes());
    }
    
    game.onmessage = function(gameMsg) {
        if (!ready) {
            console.log("not booted");
        }
        if (gameMsg.worldState.actors != null && ready) {
            var actorsMsg = gameMsg.worldState.actors.items;
            actorManager.onmessage(actorsMsg);
        }
    }

    game.isready = function() {
        return ready;
    }

	return game;
}



function ActorManager(game) {
    this.actors = {} // id -> actor
    this.onmessage = function(actorsMsg) {
        var idsEncountered = [];

        for(var i in actorsMsg) {
            var msg = actorsMsg[i];
            idsEncountered.push(msg.id);

            if (!(msg.id in this.actors)) {
                var actor = new Actor(game, msg.type);
                this.actors[msg.id] = actor;
            }

            var actor = this.actors[msg.id];
            actor.onmessage(msg);
        }

        var idsToRemove = [];
        for(var id in this.actors) {
            var id = parseInt(id);
            if (!idsEncountered.includes(id))
                idsToRemove.push(id);
        }


        for(var i in idsToRemove) {
            var id = idsToRemove[i];
            var actor = this.actors[id];
            actor.destroy();
            delete this.actors[id];
        }
    }

    this.update = function() {
        for(var id in this.actors) {
            this.actors[id].update();
        }
    }
}

ActorManager.preload = function(game) {
    var types = [0, 1];
    for (var i in types) {
        var type = types[i];
        game.load.spritesheet(  Actor.getSpriteKey(type),
                                'game/assets/' + Actor.getSpriteKey(type) + '.png',
                                FRAME_WIDTH, FRAME_HEIGHT);
    }
}

// Everything about the actor on frontend is determined by its spritesheet 'actor*.png'
// 3 frames per row for each actor
// arbitrary number of rows
// rows correspond to animations
// Add actor types to types array in ActorManager.preload()
function Actor(game, type) {

    this.sprite = game.add.sprite(0, 0, Actor.getSpriteKey(type));
    this.type = type;

    var framesPerAnimation = 3;
    var numAnimations = this.sprite.animations.frameTotal / framesPerAnimation;
    for (var i = 0; i < numAnimations; ++i) {
        var frames = [];
        for (var j = 0; j < framesPerAnimation; ++j) {
            frames.push(i * framesPerAnimation + j);
        }
        this.sprite.animations.add(i, frames, 10, true);
    }


    this.onmessage = function(msg) {
        this.sprite.x = msg.x;
        this.sprite.y = msg.y;
        this.sprite.angle = msg.angle;
        this.sprite.animations.play(msg.animation);
    }

    this.update = function() {
        this.sprite.animations.update();
    }

    this.destroy = function() {
        this.sprite.destroy();
    }
}

Actor.getSpriteKey = function(type) {
    return "actor" + type;
}