var GoBoardOperator = function(boardSize, boardUi, agehamaUi, gameControlUi, serverBaseUri) {
    this.boardSize = boardSize;
    this.boardUi = boardUi;
    this.agehamaUi = agehamaUi;
    this.gameControlUi = gameControlUi;
    this.serverBaseUri = serverBaseUri;
    this.currentBoard = null;
    this.boardStatus = null;
    this.player = null;
    this.isPlayable = false;

    this.drawClearBoard();
    this.boardUi.addCellClickListener(this.moveByPlayer.bind(this));
    this.gameControlUi.addListener(this);
};

GoBoardOperator.prototype.drawClearBoard = function() {
    for(var x=0; x<this.boardSize; x++) {
        for(var y=0; y<this.boardSize; y++) {
            this.boardUi.removeStone(x, y);
        }
    }
};

GoBoardOperator.prototype.initializeGame = function(startAsBlack) {
    this.isPlayable = false;
    $.ajax(
        {url: this.serverBaseUri + "initialBoard?size=" + this.boardSize}
    ).done(this.createInitialBoardHandler(startAsBlack));
};

GoBoardOperator.prototype.createInitialBoardHandler = function(startAsBlack) {
    return function(data) {
        var stones = data.board.stones;
        for(var x=0; x<stones.length; x++) {
            for(var y=0; y<stones[x].length; y++) {
                if (stones[x][y] == "b") {
                    this.boardUi.placeBlack(x, y);
                } else if(stones[x][y] == "w") {
                    this.boardUi.placeWhite(x, y);
                } else {
                    this.boardUi.removeStone(x, y);
                }
            }
        }
        this.currentBoard = data.board;

        this.boardStatus = new GoBoardStatus();
        this.agehamaUi.update(this.boardStatus.black, this.boardStatus.white);

        if(startAsBlack) {
            this.isPlayable = true;
        } else {
            this.moveByAi();
        }

        this.gameControlUi.gameStarted();
    }.bind(this)
};

GoBoardOperator.prototype.initialBoardHandler = function(data) {
    var stones = data.board.stones;
    for(var x=0; x<stones.length; x++) {
        for(var y=0; y<stones[x].length; y++) {
            if (stones[x][y] == "b") {
                this.boardUi.placeBlack(x, y);
            } else if(stones[x][y] == "w") {
                this.boardUi.placeWhite(x, y);
            } else {
                this.boardUi.removeStone(x, y);
            }
        }
    }
    this.currentBoard = data.board;
};

GoBoardOperator.prototype.moveByPlayer = function(x, y) {
    if (this.isPlayable) {
        this.isPlayable = false;
        $.ajax(
            {
                type: 'POST',
                contentType: 'application/json',
                url: this.serverBaseUri + "move",
                data: JSON.stringify({board: this.currentBoard, move: {x: x, y: y, s: this.currentBoard.nextPlayer}})
            }
        ).done(this.moveHandler.bind(this));
    }
};

GoBoardOperator.prototype.moveHandler = function(data) {
    if (data.success) {
        this.applyMove(data.board);
        this.moveByAi();
    } else {
        this.isPlayable = true;
    }
};

GoBoardOperator.prototype.moveByAi = function() {
    $.ajax(
        {
            type: 'POST',
            contentType: 'application/json',
            url: this.serverBaseUri + "ai/move",
            data: JSON.stringify(this.currentBoard)
        }
    ).done(this.playAiHandler.bind(this));    
};

GoBoardOperator.prototype.playAiHandler = function(data) {
    if (data.success) {
        this.applyMove(data.board);
        this.isPlayable = true;
    } else {
        alert("no move point for AI")
    }
};

GoBoardOperator.prototype.applyMove = function(board){
    this.currentBoard = board;

    this.boardStatus.addMove(board.lastMove);
    this.agehamaUi.update(this.boardStatus.agehamaBlack, this.boardStatus.agehamaWhite);

    var point = board.lastMove.point;
    if (point.s == "b") {
        this.boardUi.placeBlack(point.x, point.y)
    } else if(point.s == "w") {
        this.boardUi.placeWhite(point.x, point.y)
    }
    board.lastMove.capturedPositions.forEach(function (position) {
        this.boardUi.removeStone(position.x, position.y);
    }.bind(this));
};

GoBoardOperator.prototype.startAsBlack = function() {
    this.player = "b";
    this.initializeGame(true);
};

GoBoardOperator.prototype.startAsWhite = function() {
    this.player = "w";
    this.initializeGame(false);
};

GoBoardOperator.prototype.resignedByPlayer = function() {
    if (this.isPlayable) {
        alert("投了しました");
        this.isPlayable = false;
        this.gameControlUi.gameEnded();
    }
};

GoBoardOperator.prototype.resignedByAi = function() {
    if (this.isPlayable) {
        this.isPlayable = false;
        this.gameControlUi.gameEnded();
        $.ajax(
            {
                type: 'POST',
                contentType: 'application/json',
                url: this.serverBaseUri + "ai/learn",
                data: JSON.stringify({moves: this.boardStatus.moves, player: this.player})
            }
        ).done(this.learnPostHandler.bind(this))
    }
};

GoBoardOperator.prototype.learnPostHandler = function(data) {
    alert("手順をAIに送信しました")
};
