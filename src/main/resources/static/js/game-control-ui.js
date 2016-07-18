var GameControlUi = function(startAsBlackButton, startAsWhiteButton) {
    this.startAsBlackCallbacks = [];
    this.startAsWhiteCallbacks = [];

    $(startAsBlackButton).click(this.startAsBlackButtonClicked.bind(this));
    $(startAsWhiteButton).click(this.startAsWhiteButtonClicked.bind(this));
};

GameControlUi.prototype.addListener = function(listener) {
    this.startAsBlackCallbacks.push(listener.startAsBlack.bind(listener));
    this.startAsWhiteCallbacks.push(listener.startAsWhite.bind(listener));
};

GameControlUi.prototype.startAsBlackButtonClicked = function(event) {
    this.startAsBlackCallbacks.forEach(function (callback) { callback(); });
};

GameControlUi.prototype.startAsWhiteButtonClicked = function(event) {
    this.startAsWhiteCallbacks.forEach(function (callback) { callback(); });
};
