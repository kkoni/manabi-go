var Go9Board = function (canvas, size) {
    this.canvas = canvas;
    this.boardSize = size;
    var cellSize = size / 9;
    this.cellSize = cellSize;
    var cellClickListeners = [];
    this.cellClickListeners = cellClickListeners;

    this.cells = new Array(9);
    for (var x=0; x<9; x++) {
        this.cells[x] = new Array(9);
        for (var y=0; y<9; y++) {
            this.cells[x][y] = new Go9Cell(x, y);
            this.drawCell(this.cells[x][y], '');
        }
    }

    this.canvas.addEventListener('click', onClick, false);

    function onClick(e) {
        var rect = e.target.getBoundingClientRect();
        var clickX = e.clientX - rect.left;
        var clickY = e.clientY - rect.top;
        var cellX = Math.floor(clickX / cellSize);
        var cellY = Math.floor(clickY / cellSize);
        cellClickListeners.forEach(function (listener) {
            listener(cellX, cellY);
        });
    }
};

Go9Board.prototype.addCellClickListener = function(listener) {
    this.cellClickListeners.push(listener);
};

Go9Board.prototype.placeBlack = function(x, y) {
    this.drawCell(this.cells[x][y], 'b');
};

Go9Board.prototype.placeWhite = function(x, y) {
    this.drawCell(this.cells[x][y], 'w');
};

Go9Board.prototype.removeStone = function(x, y) {
    this.drawCell(this.cells[x][y], '');
};

Go9Board.prototype.drawCell = function(cell, stone) {
    var startX = cell.x * this.cellSize;
    var startY = cell.y * this.cellSize;
    var endX = startX + this.cellSize;
    var endY = startY + this.cellSize;
    var centerX = startX + this.cellSize/2 + 1;
    var centerY = startY + this.cellSize/2 + 1;

    var ctx = this.canvas.getContext('2d');

    ctx.fillStyle = 'rgb(204, 153, 51)';
    ctx.fillRect(startX, startY, this.cellSize, this.cellSize);

    ctx.strokeStyle = 'rgb(0, 0, 0)';
    ctx.beginPath();
    if(cell.isLeftSide()) {
        ctx.moveTo(centerX, centerY);
        ctx.lineTo(endX, centerY);
    } else if(cell.isRightSide()) {
        ctx.moveTo(startX, centerY);
        ctx.lineTo(centerX, centerY);
    } else {
        ctx.moveTo(startX, centerY);
        ctx.lineTo(endX, centerY);
    }
    ctx.stroke();

    ctx.beginPath();
    if(cell.isUpperSide()) {
        ctx.moveTo(centerX, centerY);
        ctx.lineTo(centerX, endY);
    } else if(cell.isLowerSide()) {
        ctx.moveTo(centerX, startY);
        ctx.lineTo(centerX, centerY);
    } else {
        ctx.moveTo(centerX, startY);
        ctx.lineTo(centerX, endY);
    }
    ctx.stroke();

    if(cell.isStarPoint()) {
        ctx.fillStyle = 'rgb(0, 0, 0)';
        ctx.beginPath();
        ctx.arc(centerX, centerY, this.cellSize / 15, 0, Math.PI*2, false);
        ctx.fill();
    }

    if(stone == 'b' || stone == 'w') {
        ctx.beginPath();
        if(stone == 'b') {
            ctx.fillStyle = 'rgb(0, 0, 0)';
        } else {
            ctx.fillStyle = 'rgb(255, 255, 255)';
        }
        ctx.arc(centerX, centerY, this.cellSize/2 - 2, 0, Math.PI*2, false);
        ctx.fill();

        ctx.beginPath();
        ctx.arc(centerX, centerY, this.cellSize/2 - 2, 0, Math.PI*2, false);
        ctx.stroke();
    }
};


var Go9Cell = function(x, y) {
    this.x = x;
    this.y = y;
};

Go9Cell.prototype.isLeftSide = function() {
    return this.x == 0;
};

Go9Cell.prototype.isRightSide = function() {
    return this.x == 8;
};

Go9Cell.prototype.isUpperSide = function() {
    return this.y == 0;
};

Go9Cell.prototype.isLowerSide = function() {
    return this.y == 8;
};

Go9Cell.prototype.isStarPoint = function() {
    return (this.x == 2 || this.x == 6) && (this.y == 2 || this.y == 6);
};

