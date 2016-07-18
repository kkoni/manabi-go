var GoBoardStatus = function() {
    this.agehamaBlack = 0;
    this.agehamaWhite = 0;
    this.moves = [];
};

GoBoardStatus.prototype.addMove = function(move) {
    this.moves.push(move.point);
    if (move.point.s == "b") {
        this.agehamaBlack += move.capturedPositions.length;
    } else if (move.point.s == "w") {
        this.agehamaWhite += move.capturedPositions.length;
    }
};

GoBoardStatus.prototype.clear = function() {
    this.agehamaBlack = 0;
    this.agehamaWhite = 0;
    this.moves = [];
};
