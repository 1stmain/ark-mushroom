import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;

public class KinectHelper extends J4KSDK
{
    private KinectHelperCallback kinectHelperCallback;
    private float oldRightX = 0;
    private float oldRightY = 0;
    private float oldRightZ = 0;
    private boolean isPushed = false;
    private boolean isInitialised = false;

    KinectHelper(KinectHelperCallback kinectHelperCallback)
    {
        if (!System.getProperty("os.arch").toLowerCase().contains("64"))
        {
            System.out.println("WARNING: You are running a 32 bit version of Java.");
            System.out.println("Doing so may significantly reduce the performance of this application.");
        }

        this.kinectHelperCallback = kinectHelperCallback;
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
        float leftHandY = skeleton.get2DJoint(Skeleton.HAND_LEFT, Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT)[1];
        float headY = skeleton.get2DJoint(Skeleton.HEAD, Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT)[1];

        if (!isInitialised)
        {
            oldRightZ = rightHandZ;
            isInitialised = true;
        }

        /*System.out.println(oldRightZ + "" + rightHandZ);
        if (oldRightY < rightHandY && rightHandY - oldRightY > 200)
        {
            System.out.println("swipe down ");
        }
        if (oldRightX < rightHandX && rightHandX - oldRightX > 200)
        {
            System.out.println("swipe right");
        }
        System.out.println(handpush + "" + rightHandZ + " " + oldRightZ);*/

        if (leftHandY < headY && rightHandY < headY)
        {
            kinectHelperCallback.onBothHandsRaised();
        }

        if (rightHandZ < oldRightZ && oldRightZ - rightHandZ > 0.2)
        {
            isPushed = true;
            kinectHelperCallback.onRightHandPushed(true);
        }

        if (oldRightZ - rightHandZ < 0.01 && isPushed)
        {
            isPushed = false;
            kinectHelperCallback.onRightHandPushed(false);
        }


        kinectHelperCallback.onRightHandMoved(rightHandX, rightHandY);

        oldRightX = rightHandX;
        oldRightY = rightHandY;
    }
}
