var GameControlUi = function(startAsBlackButton, startAsWhiteButton, resignedByPlayerButton, resignedByAiButton) {
    this.startAsBlackButton = startAsBlackButton;
    this.startAsWhiteButton = startAsWhiteButton;
    this.resignedByPlayerButton = resignedByPlayerButton;
    this.resignedByAiButton = resignedByAiButton;

    this.startAsBlackCallbacks = [];
    this.startAsWhiteCallbacks = [];
    this.resignedByPlayerCallbacks = [];
    this.resignedByAiCallbacks = [];

    $(startAsBlackButton).click(this.startAsBlackButtonClicked.bind(this));
    $(startAsWhiteButton).click(this.startAsWhiteButtonClicked.bind(this));
    $(resignedByPlayerButton).click(this.resignedByPlayerButtonClicked.bind(this));
    $(resignedByAiButton).click(this.resignedByAiButtonClicked.bind(this));

    this.gameEnded();
};

GameControlUi.prototype.addListener = function(listener) {
    this.startAsBlackCallbacks.push(listener.startAsBlack.bind(listener));
    this.startAsWhiteCallbacks.push(listener.startAsWhite.bind(listener));
    this.resignedByPlayerCallbacks.push(listener.resignedByPlayer.bind(listener));
    this.resignedByAiCallbacks.push(listener.resignedByAi.bind(listener));
};

GameControlUi.prototype.startAsBlackButtonClicked = function(event) {
    this.startAsBlackCallbacks.forEach(function (callback) { callback(); });
};

GameControlUi.prototype.startAsWhiteButtonClicked = function(event) {
    this.startAsWhiteCallbacks.forEach(function (callback) { callback(); });
};

GameControlUi.prototype.resignedByPlayerButtonClicked = function(event) {
    this.resignedByPlayerCallbacks.forEach(function (callback) { callback(); });
};

GameControlUi.prototype.resignedByAiButtonClicked = function(event) {
    this.resignedByAiCallbacks.forEach(function (callback) { callback(); });
};

GameControlUi.prototype.gameStarted = function() {
    $(this.startAsBlackButton).prop("disabled", false);
    $(this.startAsWhiteButton).prop("disabled", false);
    $(this.resignedByPlayerButton).prop("disabled", false);
    $(this.resignedByAiButton).prop("disabled", false);
};

GameControlUi.prototype.gameEnded = function() {
    $(this.startAsBlackButton).prop("disabled", false);
    $(this.startAsWhiteButton).prop("disabled", false);
    $(this.resignedByPlayerButton).prop("disabled", true);
    $(this.resignedByAiButton).prop("disabled", true);
};
