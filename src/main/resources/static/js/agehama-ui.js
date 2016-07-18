var AgehamaUi = function(blackNode, whiteNode) {
    this.blackNode = blackNode;
    this.whiteNode = whiteNode;
};

AgehamaUi.prototype.update = function(black, white) {
    $(this.blackNode).text(black);
    $(this.whiteNode).text(white);
};
