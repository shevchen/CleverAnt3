package core;

public enum Direction {
	LEFT, RIGHT, UP, DOWN;

	public Direction rotateLeft() {
		switch (this) {
		case LEFT:
			return DOWN;
		case RIGHT:
			return UP;
		case UP:
			return LEFT;
		case DOWN:
			return RIGHT;
		}
		throw new RuntimeException();
	}

	public Direction rotateRight() {
		switch (this) {
		case LEFT:
			return UP;
		case RIGHT:
			return DOWN;
		case UP:
			return RIGHT;
		case DOWN:
			return LEFT;
		}
		throw new RuntimeException();
	}
}
