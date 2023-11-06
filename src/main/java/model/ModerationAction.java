package model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ModerationAction {
    int aid;
    int pid;
    ModerationType actionType;

    public ModerationAction() {

    }
    public ModerationAction(int aid, int pid, ModerationType actionType) {
        this.aid = aid;
        this.pid = pid;
        this.actionType = actionType;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setActionType(ModerationType actionType) {
        this.actionType = actionType;
    }

    public int getAid() {
        return aid;
    }

    public int getPid() {
        return pid;
    }

    public ModerationType getActionType() {
        return actionType;
    }
}
