var FRAME_WIDTH = 32;
var FRAME_HEIGHT = 36;
var MAX_HP = 300;

function CreateGame(parent, socket, lobbyIndex, mapJson) {
	var game = new Phaser.Game(
			            1600, 900, Phaser.AUTO, parent,
			            {
			            	preload: preload, 
			            	create: create, 
			            	update: update
			            }
		        	);
	var actorManager = new ActorManager(game);
	var feedbackManager = new FeedbackManager(actorManager, game);
    var cursors;
	var q,w,e,r;
	var inputMessage = null;
	var ready = false;

    function preload() {
        //game.load.image('tile', 'game/assets/map.png');    	
        this.game.load.tilemap('MyTilemap', null, mapJson, Phaser.Tilemap.TILED_JSON);
        this.game.load.image('tile-set', 'game/assets/tiles.png');
        
        ActorManager.preload(game);
    }
    
    function create() {
    	var Background = game.add.group();
    	var SpriteLevel = game.add.group();
        var map = this.game.add.tilemap('MyTilemap');
        map.addTilesetImage('tiles', 'tile-set');
        Background.add(map.createLayer('Cosmetic'));
        
    	ready = true;
    	
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
        if (gameMsg.worldState.players != null && ready) {
        	var playersMsg = gameMsg.worldState.players.items;
        	var cooldownsMsg = gameMsg.worldState.skillsCooldown.items;
        	feedbackManager.onmessage(playersMsg, cooldownsMsg);
        }
    }

    game.isready = function() {
        return ready;
    }

	return game;
}

function FeedbackManager(actorManager, game) {
	var feedback = document.querySelector("#feedback");
	var style = { font: "65px Arial", fill: "#ff0044", align: "center" };
	this.bars = {}


	this.onmessage = function(playersMsg, cooldownsMsg) {
		var info = "";
		//var hpStats = "<div>";
		for (var i in playersMsg) {
			//hpStats += "Player " + i + ": ";
			//hpStats += playersMsg[i];
			//hpStats += "<br>";
			var player = playersMsg[i];
			if ( player.id in actorManager.actors ){
				var sprite = actorManager.actors[player.id].sprite;
				if ( !(player.id in this.bars)){				
					var hpBar = new HealthBar(game, {x: sprite.x, y: sprite.y - 10, width: 32, height: 4});
					hpBar.setPercent(100);
					hpBar.setBarColor('#FFFF00');
					this.bars[player.id] = hpBar;
				} else {
					if (player.hp <= 0){
						this.bars[player.id].kill();
						delete this.bars[player.id];
					} else {
						this.bars[player.id].setPosition(sprite.x, sprite.y - 20);
						this.bars[player.id].setPercent( 100 * player.hp / MAX_HP );
					}
				}
			}
		}
		//hpStats += "</div><br>"
		//info += hpStats;
		var cooldownStats = "<div>"
	    
		for (let i in cooldownsMsg) {
			var skillInfo = cooldownsMsg[i];
			cooldownStats += skillInfo.skillType;
			cooldownStats += ": ";
			cooldownStats += skillInfo.cooldown;
			cooldownStats += "<br>"
		}
		cooldownStats += "</div>";
		info += cooldownStats;
		feedback.innerHTML = info;
	}
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


/*ActorManager.type2imagenames = {
    0 : ['Sylv_debug.png'],
    1 : ['fireball_debug.png']
}*/


ActorManager.type2imagenames = {
    0 : ['Sylv.png'],
    1 : ['fireball.png']
}

ActorManager.preload = function(game) {
    for (var type in ActorManager.type2imagenames) {
        var imagenames = ActorManager.type2imagenames[type];
        for (var i in imagenames) {
            var name = imagenames[i];
            game.load.spritesheet(  name,
                                    'game/assets/' + name,
                                    FRAME_WIDTH, FRAME_HEIGHT);
        }
    }
}

// Everything about the actor on frontend is determined by its spritesheet 'actor*.png'
// 3 frames per row for each actor
// arbitrary number of rows
// rows correspond to animations
// Add actor types to types array in ActorManager.preload()
function Actor(game, type) {

    this.sprite = game.add.sprite(0, 0, Actor.getRandomSpriteKey(type));
    this.sprite.anchor.x = 0.5;
    this.sprite.anchor.y = 0.5; // position by center, not top left corner
    this.type = type;
    //console.log(game)
    //SpriteLevel.add(this.sprite);
    game.world.children[1].add(this.sprite);
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

Actor.getRandomSpriteKey = function(type) {
    var imagenames = ActorManager.type2imagenames[type];
    var name = imagenames[Math.floor(Math.random() * imagenames.length)];
    return name;
}