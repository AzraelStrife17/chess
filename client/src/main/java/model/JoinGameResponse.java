package model;

import chess.ChessGame;

public record JoinGameResponse(int gameID, ChessGame game) {
}
