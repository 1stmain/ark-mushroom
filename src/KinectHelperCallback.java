public interface KinectHelperCallback
{
    void onRightHandMoved(float x, float y);

    void onRightHandPushed(boolean rightHandIsPushed);

    void onBothHandsRaised();
}
