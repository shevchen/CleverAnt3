package core;

public enum Turn {
	MOVE, ROTATELEFT, ROTATERIGHT;

	public String getRuType() {
		switch (this) {
		case MOVE:
			return "Движение вперёд";
		case ROTATELEFT:
			return "Поворот против часовой стрелки";
		case ROTATERIGHT:
			return "Поворот по часовой стрелке";
		}
		throw new RuntimeException();
	}
}
