var go9Board = new Go9Board(document.getElementById("go-board"), 585);
var agehamaUi = new AgehamaUi(document.getElementById("agehama-black"), document.getElementById("agehama-white"));
var go9BoardOperator = new Go9BoardOperator(go9Board, agehamaUi, "http://localhost:8080/");
