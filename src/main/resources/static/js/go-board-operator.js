var Go9BoardOperator = function(boardUi, serverBaseUri) {
    this.boardUi = boardUi;
    this.serverBaseUri = serverBaseUri;
    this.currentBoard = null;
    this.isPlayable = true;

    this.initialize();
    this.boardUi.addCellClickListener(this.moveByPlayer.bind(this));
};

Go9BoardOperator.prototype.initialize = function() {
    $.ajax(
        {url: this.serverBaseUri + "initialBoard?size=9"}
    ).done(this.initialBoardHandler.bind(this));
};

Go9BoardOperator.prototype.initialBoardHandler = function(data) {
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

Go9BoardOperator.prototype.moveByPlayer = function(x, y) {
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

Go9BoardOperator.prototype.moveHandler = function(data) {
    if (data.success) {
        this.applyMove(data.board);
        this.moveByAi();
    } else {
        this.isPlayable = true;
    }
};

Go9BoardOperator.prototype.moveByAi = function() {
    $.ajax(
        {
            type: 'POST',
            contentType: 'application/json',
            url: this.serverBaseUri + "ai/move",
            data: JSON.stringify(this.currentBoard)
        }
    ).done(this.playAiHandler.bind(this));    
};

Go9BoardOperator.prototype.playAiHandler = function(data) {
    if (data.success) {
        this.applyMove(data.board);
        this.isPlayable = true;
    } else {
        alert("no move point for AI")
    }
};

Go9BoardOperator.prototype.applyMove = function(board){
    this.currentBoard = board;
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
