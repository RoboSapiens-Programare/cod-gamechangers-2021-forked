package org.firstinspires.ftc.teamcode.drive;

public abstract class Subsystem {

    protected enum SubMode {
        SUB_IDLE,
        SUB_BUSY
    }

//    protected SubMode subMode;

    protected abstract void updateSub();

    protected void waitForSubIdle(SubMode mode) {
        while (!Thread.currentThread().isInterrupted() && isSubBusy(mode)) {
            updateSub();
        }
    }

    protected boolean isSubBusy(SubMode mode) { return mode != SubMode.SUB_IDLE; }
}