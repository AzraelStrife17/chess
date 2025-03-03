package model;
import chess.ChessGame;

public record JoinGameRecord(ChessGame.TeamColor playerColor, Integer gameID) {
}
