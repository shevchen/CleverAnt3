package core;

public enum Mutation {
	ACTION_MUTATION, NEXT_STATE_MUTATION, SIGNIFICANT_PREDICATE_MUTATION, START_STATE_MUTATION;

	public String getRuType() {
		switch (this) {
		case ACTION_MUTATION:
			return "выходного воздействия";
		case NEXT_STATE_MUTATION:
			return "номера следующего состояния";
		case SIGNIFICANT_PREDICATE_MUTATION:
			return "значимого предиката";
		case START_STATE_MUTATION:
			return "номера стартового состояния";
		}
		throw new RuntimeException();
	}
}
