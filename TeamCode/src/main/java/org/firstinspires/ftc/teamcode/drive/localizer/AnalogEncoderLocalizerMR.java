package org.firstinspires.ftc.teamcode.drive.localizer;

//import android.support.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.TwoTrackingWheelLocalizer;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

import static java.lang.Math.abs;

//import com.qualcomm.robotcore.hardware.AnalogSensor;

public class AnalogEncoderLocalizerMR extends TwoTrackingWheelLocalizer {
    public static double WHEEL_RADIUS = 4; // inch, care e defapt diametrul but oh well
    public static double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed
    public static double MAX_VOLTAGE = 3.27;

    public static double ENCODER_RATIO = 1.0; // ratio between left encoder ticks per rev and right encoder ticks per rev

    public static double LATERAL_DISTANCE = 14 ; // inch; distance between the left and right wheels
    public static double FORWARD_OFFSET = 0; // inch; offset of the lateral wheel

    private absoluteEncoder middleEncoder = new absoluteEncoder();
    private absoluteEncoder rightEncoder = new absoluteEncoder();

    private ModernRoboticsI2cGyro gyro;
    private double lastAngle, globalAngle;

    public AnalogEncoderLocalizerMR(HardwareMap hardwareMap) {
        super(Arrays.asList(
                new Pose2d(0, -LATERAL_DISTANCE / 2, 0), // right
                new Pose2d(FORWARD_OFFSET, 0, Math.toRadians(90)) // front
        ));


        middleEncoder.encoder = hardwareMap.get(AnalogInput.class, "middleEncoder");
        rightEncoder.encoder = hardwareMap.get(AnalogInput.class, "rightEncoder");

        middleEncoder.setInitVolt(middleEncoder.encoder.getVoltage());
        rightEncoder.setInitVolt(rightEncoder.encoder.getVoltage());

        gyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");
        gyro.calibrate();
        while (gyro.isCalibrating()){
        }
    }

    public static double voltageToInches(double pos) {
        return ((pos * WHEEL_RADIUS * Math.PI) / MAX_VOLTAGE);
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        return Arrays.asList(
                voltageToInches(rightEncoder.getVoltageWithIndex()),
                voltageToInches(middleEncoder.getVoltageWithIndex())
        );
    }


    @Override
    public double getHeading() {
        double current = gyro.getHeading();

        double delta = current-lastAngle;
        if(delta > 180) {
            delta-=360;
        } else if (delta < -180){
            delta +=360;
        }

        lastAngle = current;
        globalAngle +=delta;
        return globalAngle;
    }

    public List<Double> getVoltages() {
        return Arrays.asList(
                rightEncoder.toThreeDec(rightEncoder.readVoltage()),
                middleEncoder.toThreeDec(middleEncoder.readVoltage())
        );
    }

    public List <Double> getDerivatives(){
        return Arrays.asList(
                rightEncoder.getDerivative(),
                middleEncoder.getDerivative()
        );
    }

    public List <Double> getTotalVoltages(){
        return Arrays.asList(
                rightEncoder.getTotalVoltage(),
                middleEncoder.getTotalVoltage()
        );
    }

    public List <Double> getTotalVoltagesWithIndex(){
        return Arrays.asList(
                rightEncoder.getVoltageWithIndex(),
                middleEncoder.getVoltageWithIndex()
        );
    }

    public List <Double> getIndex(){
        return Arrays.asList(
                rightEncoder.getTurnIndex(),
                middleEncoder.getTurnIndex()
        );
    }

    public static double toThreeDec(double num) {
        DecimalFormat numberFormat = new DecimalFormat("#.000");
        return Double.parseDouble(numberFormat.format(num));
    }
}


