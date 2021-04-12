package org.firstinspires.ftc.teamcode.drive.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.drive.Subsystem;

public class   ThrowingMechanism extends Subsystem {
    private DcMotorEx throwWheelMotor;
    private Servo pushServo;

    private ElapsedTime timer;

    private double SERVO_PUSHED = 1, SERVO_REST = 0;
    //TODO this is an arbitrary value
    private int SPIN_TIME = 5000;
    private int PUSH_TIME = 1000;

    public ThrowingMechanism(HardwareMap hardwareMap){
        throwWheelMotor = hardwareMap.get(DcMotorEx.class, "motorAruncare");

        throwWheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        throwWheelMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        throwWheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        pushServo = hardwareMap.servo.get("servoImpins");
        pushServo.setPosition(SERVO_REST);

        subMode = SubMode.SUB_IDLE;

        timer = new ElapsedTime();
    }

    public double getPower(){ return throwWheelMotor.getPower(); }

    public double getVelo(){ return throwWheelMotor.getVelocity();}

    public double getCurrent(){ return  throwWheelMotor.getCurrent(CurrentUnit.AMPS); }

    public void stop(){ throwWheelMotor.setPower(0); }

    public void pushRingAsync(boolean push){
        pushServo.setPosition(push? SERVO_PUSHED : SERVO_REST);
        timer.reset();
        subMode = SubMode.SERVO;
    }

    public void pushRing(boolean push){
        pushRingAsync(push);
        waitForSubIdle();
    }

    //TODO i think this is messy and unecessary dar acm e o singura functie
    public void pushRing(){
        pushRingAsync(true);
        waitForSubIdle();
        pushRingAsync(false);
        waitForSubIdle();
    }

    public void rotateAsync(double power){
//        power = Range.clip(power, -0.9, 0.9);
        throwWheelMotor.setPower(power);
        timer.reset();
        subMode = SubMode.SUB_BUSY;
    }

    public void rotateAtSpeedAsync(double speed){
        throwWheelMotor.setVelocity(speed);
        timer.reset();
        subMode = SubMode.SUB_BUSY;
    }

    public void rotate(double power){
        rotateAsync(power);
        waitForSubIdle();
    }

    public void rotateAtSpeed(double speed){
        rotateAtSpeedAsync(speed);
        waitForSubIdle();
    }

    @Override
    protected void updateSub() {
        switch (subMode){
            case SUB_IDLE:
                //do nothing
                break;
            case SUB_BUSY:
                if(timer.milliseconds() >= SPIN_TIME){
                    stop();
                    subMode = SubMode.SUB_IDLE;
                }
                break;
            case SERVO:
                if(timer.milliseconds() >= PUSH_TIME){
                    stop();
                    subMode = SubMode.SUB_IDLE;
                }
                break;

        }
    }
}
