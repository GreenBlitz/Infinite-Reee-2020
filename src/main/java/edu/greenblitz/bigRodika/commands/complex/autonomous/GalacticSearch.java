package edu.greenblitz.bigRodika.commands.complex.autonomous;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.intake.ExtendAndCollect;
import edu.greenblitz.bigRodika.commands.intake.RetractAndStop;
import edu.greenblitz.bigRodika.commands.intake.roller.RollByConstant;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import org.greenblitz.motion.base.State;

import java.util.ArrayList;

public class GalacticSearch extends ParallelCommandGroup {

    long startTime;


    public GalacticSearch(){

        ArrayList<State> redPathB = new ArrayList<>(){{

            add(new State(razToMeter(1.52), razToMeter(5),-2.23,0,0));
            add(new State(razToMeter(3), razToMeter(4),-2.23,3.6,7));
            add(new State(razToMeter(5), razToMeter(2),-1.55,3.6,9.5));
            add(new State(razToMeter(8), razToMeter(4),-2.2,3.6,-9.5));
            add(new State(razToMeter(10), razToMeter(-3),-2.7,0,0));

        }};
        ArrayList<State> bluePathA = new ArrayList<>(){
            {
                add(new State(razToMeter(1.52), razToMeter(5), -2.23, 0, 0));
                add(new State(razToMeter(3), razToMeter(4), -2.23, 3.6, 7));
                add(new State(razToMeter(5), razToMeter(2), -1.55, 3.6, 9.5));
                add(new State(razToMeter(8), razToMeter(4), -2.2, 3.6, -9.5));
                add(new State(razToMeter(10), razToMeter(-3), -2.7, 0, 0));
            }};
        Follow2DProfileCommand command = new Follow2DProfileCommand(bluePathA, RobotMap.Limbo2.Chassis.MotionData.CONFIG, 0.5
                , false);

        command.setSendData(false);


        addCommands(
                new SequentialCommandGroup(
                        new ExtendAndCollect(0.7){
                            @Override
                            public boolean isFinished(){
                                return (System.currentTimeMillis() - startTime)/1000.0 > 7;
                            }
                        },
                        new RetractAndStop()
                ),

                new SequentialCommandGroup(
                        new WaitCommand(3.5),
                        new ThreadedCommand(command, Chassis.getInstance())
                )
        );
    }

    @Override
    public void initialize(){
        super.initialize();
        startTime = System.currentTimeMillis();

    }


    public static double razToMeter(double raz){
        return 0.762 * raz;
    }
}
