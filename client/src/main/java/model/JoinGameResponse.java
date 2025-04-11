package model;

import chess.ChessGame;

public record JoinGameResponse(String result, ChessGame game) {
}
