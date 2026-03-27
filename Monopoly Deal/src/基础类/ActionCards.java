package 基础类;

public class ActionCards extends Card {
    private ActionCardType actionCardType;

    public ActionCards(ActionCardType actionCardType) {
        this.actionCardType = actionCardType;
    }

    public ActionCardType getActionCardType() {
        return actionCardType;
    }

    public void setActionCardType(ActionCardType actionCardType) {
        this.actionCardType = actionCardType;
    }
}
