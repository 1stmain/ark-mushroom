import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;

public class KinectHelper extends J4KSDK
{
    private KinectHelperCallback kinectHelperCallback;
    public float oldRightX =0 ;
    public float oldRightY = 0;
    public float oldRightZ = 0;
    public int handpush = 0;
    public int intialise = 0;
    KinectHelper(KinectHelperCallback kinectHelperCallback)
    {
        if (!System.getProperty("os.arch").toLowerCase().contains("64"))
        {
            System.out.println("WARNING: You are running a 32 bit version of Java.");
            System.out.println("Doing so may significantly reduce the performance of this application.");
        }

        //start(J4KSDK.SKELETON);

        this.kinectHelperCallback = kinectHelperCallback;

        /*try
        {
            Thread.sleep(30000);
        }
        catch (InterruptedException e)
        {
            System.out.println(Constants.SLEEP_ERROR);
        }

        stop();*/
    }

    @Override
    public void onDepthFrameEvent(short[] shorts, byte[] bytes, float[] floats, float[] floats1)
    {
        // This method is never used
    }

    @Override
    public void onColorFrameEvent(byte[] bytes)
    {
        // This method is never used
    }

    @Override
    public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] floats, float[] floats1, byte[] bytes)
    {
        int skeletonNumber = 0;
        for (boolean isSkeletonTracked : skeleton_tracked)
        {
            if (isSkeletonTracked)
            {
                break;
            }
            skeletonNumber++;
        }

        Skeleton skeleton = getSkeletons()[skeletonNumber];

        float rightHandX = skeleton.get2DJoint(Skeleton.HAND_RIGHT, Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT)[0];
        float rightHandY = skeleton.get2DJoint(Skeleton.HAND_RIGHT, Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT)[1];
        float rightHandZ = skeleton.get3DJointZ(Skeleton.HAND_RIGHT);
//        System.out.println(rightHandZ);
        if(intialise == 0){
                oldRightZ = rightHandZ;
            intialise = 1;
        }
//        System.out.println(oldRightZ + "" + rightHandZ);
//                if(oldRightY < rightHandY && rightHandY - oldRightY > 200 ){
//                    System.out.println("swipe down ");
//                }
//                if(oldRightX < rightHandX && rightHandX - oldRightX > 200){
//                    System.out.println("swripe right");
//                }
//                System.out.println(handpush + "" + rightHandZ + " " + oldRightZ );
               if(rightHandZ < oldRightZ && oldRightZ -  rightHandZ  > 0.4){
                   System.out.println("hand on ");
                    handpush  =1;
                }
               if( oldRightZ - rightHandZ  < 0.01 && handpush == 1 ){
                    System.out.println("hand off ");
                    handpush = 0 ;
              }
                oldRightX = rightHandX;
                oldRightY  = rightHandY;
        }
}
