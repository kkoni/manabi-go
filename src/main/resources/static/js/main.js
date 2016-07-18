var go9Board = new Go9Board(document.getElementById("go-board"), 585);
var agehamaUi = new AgehamaUi(
    document.getElementById("agehama-black"),
    document.getElementById("agehama-white")
);
var gameControlUi = new GameControlUi(
    document.getElementById("start-as-black"),
    document.getElementById("start-as-white"),
    document.getElementById("resigned-by-player"),
    document.getElementById("resigned-by-ai")
);
var goBoardOperator = new GoBoardOperator(9, go9Board, agehamaUi, gameControlUi, "http://localhost:8080/");
