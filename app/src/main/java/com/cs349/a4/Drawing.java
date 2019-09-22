package com.cs349.a4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class Drawing extends View {

    Segment torso;
    Segment curSeg = null;
    PointF start;
    PointF pos;

    private float scaleFactor = 1.f;
    private ScaleGestureDetector scaleGestureDetector;

    public void reset() {
        torso = null;
        start = null;
        pos = null;
        curSeg = null;
        init();
        this.invalidate();
    }

    public Drawing(Context c, AttributeSet atts) {
        super(c, atts);
        setOnTouchListener(new TouchListener());
        scaleGestureDetector = new ScaleGestureDetector(c, new ScaleListener());
        init();
    }

    public void init() {


        // head
        Segment head = new Segment(new RectF(-Config.headW / 2, -Config.headH,Config.headW / 2,
                0), Config.SegType.HEAD);
        head.imageLeft = -Config.headW / 2-30;
        head.imageTop = -Config.headH - 30;

        // torso
        torso = new Segment(new RectF(0,0, Config.torsoW,Config.torsoL - 100),
                Config.SegType.TORSO);
        torso.pos = new PointF(1000,250);
        torso.curPos = torso.pos;
        torso.imageLeft = -10;
        torso.imageTop = 0;

        // upper arm
        Segment leftUpperArm = new Segment(new RectF(-Config.armW / 2, 0,Config.armW / 2,
                Config.upperArmL),Config.SegType.LEFTUPPERARM);
        leftUpperArm.degree = 20;
        leftUpperArm.imageLeft = -Config.armW / 2 - 40;
        leftUpperArm.imageTop = 0;
        Segment rightUpperArm = new Segment(new RectF(-Config.armW / 2,0,Config.armW / 2,
                Config.upperArmL), Config.SegType.RIGHTUPPERARM);
        rightUpperArm.degree = -20;
        rightUpperArm.imageLeft = -Config.armW / 2 + 30;
        rightUpperArm.imageTop = 0;

        // lower arm
        Segment leftLowerArm = new Segment(new RectF(-Config.armW/2+30,0,Config.armW/2-30,
                Config.lowerArmL), Config.SegType.LEFTLOWERARM);
        leftLowerArm.imageLeft = -Config.armW / 2+10;
        leftLowerArm.imageTop = 0;
        Segment rightLowerArm = new Segment(new RectF(-Config.armW/2+30,-10,Config.armW/2-30,
                Config.lowerArmL), Config.SegType.RIGHTLOWERARM);
        rightLowerArm.imageLeft = -Config.armW / 2 + 70;
        rightLowerArm.imageTop = -45;

        // hand
        Segment leftHand = new Segment(new RectF(-Config.handW/2,0,Config.handW/2,
                Config.handL), Config.SegType.HAND);
        leftHand.imageLeft = -Config.handW / 2 + 30;
        leftHand.imageTop = -30;
        Segment rightHand = new Segment(new RectF(-Config.handW/2,0,Config.handW/2,
                Config.handL), Config.SegType.HAND);
        rightHand.imageLeft = -Config.handW / 2 + 30;
        rightHand.imageTop = 0;

        // upper leg
        Segment leftUpperLeg = new Segment(new RectF(-100,-10,Config.legW/2,
                Config.upperLegL), Config.SegType.LEFTUPPERLEG);
        leftUpperLeg.degree = 0;
        leftUpperLeg.imageLeft = -135;
        leftUpperLeg.imageTop = -15;
        Segment rightUpperLeg = new Segment(new RectF(-120,-10,Config.legW/2,
                Config.upperLegL), Config.SegType.RIGHTUPPERLEG);
        rightUpperLeg.degree = 0;
        rightUpperLeg.imageLeft = -150;
        rightUpperLeg.imageTop = -5;

        // lower leg
        Segment leftLowerLeg = new Segment(new RectF(-70,-20,120, Config.lowerLegL),
                Config.SegType.LEFTLOWERLEG);
        leftLowerLeg.imageLeft = -70;
        leftLowerLeg.imageTop = -25;
        Segment rightLowerLeg = new Segment(new RectF(-50,-20,75, Config.lowerLegL),
                Config.SegType.RIGHTLOWERLEG);
        rightLowerLeg.imageLeft = -70;
        rightLowerLeg.imageTop = -25;

        // feet
        Segment leftFeet = new Segment(new RectF(-50,0, Config.feetW, Config.feetL),
                Config.SegType.FEET);
        leftFeet.imageLeft = -50;
        leftFeet.imageTop = -25;
        Segment rightFeet = new Segment(new RectF(-50,0, Config.feetW, Config.feetL),
                Config.SegType.FEET);
        rightFeet.imageLeft = -50;
        rightFeet.imageTop = -25;

        // upper body hierarchy
        torso.addChild(head, new PointF((Config.torsoW/2-20),200));
        torso.addChild(leftUpperArm, new PointF(0,-30));
        torso.addChild(rightUpperArm, new PointF(Config.torsoW - 60,-10));
        leftUpperArm.addChild(leftLowerArm, new PointF(0, Config.upperArmL - 20));
        rightUpperArm.addChild(rightLowerArm, new PointF(60,Config.upperArmL - 40));
        leftLowerArm.addChild(leftHand, new PointF(30, Config.lowerArmL));
        rightLowerArm.addChild(rightHand, new PointF(-10, Config.lowerArmL));

        // lower body hierarchy
        torso.addChild(leftUpperLeg, new PointF(Config.torsoW/4-60, Config.torsoL-75));
        torso.addChild(rightUpperLeg, new PointF(3 * Config.torsoW / 4+25,Config.torsoL-90));
        leftUpperLeg.addChild(leftLowerLeg, new PointF(-80,Config.upperLegL-45));
        rightUpperLeg.addChild(rightLowerLeg, new PointF(80, Config.upperLegL-40));
        leftLowerLeg.addChild(leftFeet, new PointF(45, Config.lowerLegL-50));
        rightLowerLeg.addChild(rightFeet, new PointF(-20, Config.lowerLegL-50));

        // set segment bitmaps
        /* Inspired by this answer from Stack Overflow: https://stackoverflow.com/a/4955305 */
        Bitmap headBmp = BitmapFactory.decodeResource(getResources(), R.drawable.head);
        headBmp = Bitmap.createScaledBitmap(headBmp, 300, 400, false);
        Bitmap torsoBmp = BitmapFactory.decodeResource(getResources(), R.drawable.torso);
        torsoBmp = Bitmap.createScaledBitmap(torsoBmp, 400, 500, false);
        Bitmap upperArmLeftBmp = BitmapFactory.decodeResource(getResources(), R.drawable.left_upper_arm);
        upperArmLeftBmp = Bitmap.createScaledBitmap(upperArmLeftBmp, 300, 500, false);
        Bitmap upperArmRightBmp = BitmapFactory.decodeResource(getResources(), R.drawable.right_upper_arm);
        upperArmRightBmp = Bitmap.createScaledBitmap(upperArmRightBmp, 300, 500, false);
        Bitmap lowerArmLeftBmp = BitmapFactory.decodeResource(getResources(), R.drawable.left_lower_arm);
        lowerArmLeftBmp = Bitmap.createScaledBitmap(lowerArmLeftBmp, 180, 450, false);
        Bitmap lowerArmRightBmp = BitmapFactory.decodeResource(getResources(), R.drawable.right_lower_arm);
        lowerArmRightBmp = Bitmap.createScaledBitmap(lowerArmRightBmp, 150, 450, false);
        Bitmap handLeftBmp = BitmapFactory.decodeResource(getResources(), R.drawable.left_hand);
        handLeftBmp = Bitmap.createScaledBitmap(handLeftBmp, 250, 300, false);
        Bitmap handRightBmp = BitmapFactory.decodeResource(getResources(), R.drawable.right_hand);
        handRightBmp = Bitmap.createScaledBitmap(handRightBmp, 200, 300, false);

        Bitmap upperLegLeftBmp = BitmapFactory.decodeResource(getResources(), R.drawable.left_upper_leg);
        upperLegLeftBmp = Bitmap.createScaledBitmap(upperLegLeftBmp, 300, 470, false);
        Bitmap upperLegRightBmp = BitmapFactory.decodeResource(getResources(), R.drawable.right_upper_leg);
        upperLegRightBmp = Bitmap.createScaledBitmap(upperLegRightBmp, 300, 470, false);
        Bitmap lowerLegLeftBmp = BitmapFactory.decodeResource(getResources(), R.drawable.left_lower_leg);
        lowerLegLeftBmp = Bitmap.createScaledBitmap(lowerLegLeftBmp, 200, 500, false);
        Bitmap lowerLegRightBmp = BitmapFactory.decodeResource(getResources(), R.drawable.right_lower_leg);
        lowerLegRightBmp = Bitmap.createScaledBitmap(lowerLegRightBmp, 150, 500, false);
        Bitmap feetleftBmp = BitmapFactory.decodeResource(getResources(), R.drawable.left_feet);
        feetleftBmp = Bitmap.createScaledBitmap(feetleftBmp, 150, 200, false);
        Bitmap feetrightBmp = BitmapFactory.decodeResource(getResources(), R.drawable.right_feet);
        feetrightBmp = Bitmap.createScaledBitmap(feetrightBmp, 120, 230, false);

        head.image = headBmp;
        torso.image = torsoBmp;
        leftUpperArm.image = upperArmLeftBmp;
        rightUpperArm.image = upperArmRightBmp;
        leftLowerArm.image = lowerArmLeftBmp;
        rightLowerArm.image = lowerArmRightBmp;

        rightHand.image = handRightBmp;
        leftHand.image = handLeftBmp;

        leftUpperLeg.image = upperLegLeftBmp;
        rightUpperLeg.image = upperLegRightBmp;
        leftLowerLeg.image = lowerLegLeftBmp;
        rightLowerLeg.image = lowerLegRightBmp;

        leftFeet.image = feetleftBmp;
        rightFeet.image = feetrightBmp;
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        torso.drawSegment(c);
    }

    private float getRotationDegree(PointF newP, PointF oldP) {
        float rotated = (float) ((180 / Math.PI)*Math.acos((newP.y - oldP.y) /
                Math.sqrt(Math.pow(newP.x - oldP.x, 2) + Math.pow(newP.y - oldP.y, 2))));
        return rotated;
    }

    private void updateChildrenPos(Segment seg) {
        for (Segment childSeg: seg.children) {
            childSeg.curPos = new PointF(childSeg.pos.x + seg.curPos.x, childSeg.pos.y + seg.curPos.y);
            updateChildrenPos(childSeg);
        }
    }

    private class TouchListener implements OnTouchListener {
        public boolean onTouch(View v, MotionEvent me) {
            scaleGestureDetector.onTouchEvent(me);
            int direction = 1;

            if (me.getAction() == MotionEvent.ACTION_MOVE) {
                if (curSeg == null) return false;
                Config.SegType type = curSeg.type;
                switch (type) {
                    case HEAD:
                        if (me.getX() < curSeg.curPos.x) direction = -1;
                        float rotateDegree = getRotationDegree(curSeg.curPos, new PointF(me.getX(), me.getY()));
                        if (rotateDegree > Config.HEAD_LIMIT) rotateDegree = Config.HEAD_LIMIT;
                        curSeg.degree = rotateDegree * direction;
                        v.invalidate();
                        break;
                    case TORSO:
                        float posX = me.getX() - (start.x - pos.x);
                        float posY = me.getY() - (start.y - pos.y);
                        torso.pos = new PointF(posX, posY);
                        torso.curPos = torso.pos;
                        updateChildrenPos(torso);
                        v.invalidate();
                        break;
                    case HAND:
                    case FEET:
                        PointF ancestorPos = curSeg.parent.parent.curPos;
                        PointF parentPos = curSeg.parent.pos;
                        PointF parentCurPos = curSeg.parent.curPos;
                        PointF selfPos = curSeg.pos;

                        float ancestorDegree = curSeg.parent.parent.degree;
                        float parentDegree = curSeg.parent.degree + ancestorDegree;
                        double thetaAncestor = Math.toRadians((double) ancestorDegree);
                        double thetaParent = Math.toRadians((double) parentDegree);

                        PointF parentP = new PointF((float)(parentPos.x * Math.cos(thetaAncestor)
                                - parentPos.y * Math.sin(thetaAncestor)),
                                (float)(parentPos.y * Math.cos(thetaAncestor) + parentPos.x * Math.sin(thetaAncestor)));
                        PointF selfP = new PointF((float) (selfPos.x * Math.cos(thetaParent)
                                - selfPos.y * Math.sin(thetaParent)),
                                (float) (selfPos.y * Math.cos(thetaParent) + selfPos.x * Math.sin(thetaParent)));

                        curSeg.parent.curPos = new PointF(parentP.x + ancestorPos.x, parentP.y + ancestorPos.y);
                        curSeg.curPos = new PointF(selfP.x + parentCurPos.x, selfP.y + parentCurPos.y);

                        if (me.getX() >= curSeg.curPos.x) direction = -1;

                        float curDegree = (float) 180 - getRotationDegree(curSeg.curPos, new PointF(me.getX(), me.getY()));
                        float degree = curDegree - (ancestorDegree + curSeg.parent.degree) * direction;

                        if (degree < -Config.HAND_FEET_LIMIT) degree = -Config.HAND_FEET_LIMIT;
                        if (degree > Config.HAND_FEET_LIMIT) degree = Config.HAND_FEET_LIMIT;

                        curSeg.degree = degree * direction;
                        curSeg.curDegree = curDegree;
                        v.invalidate();
                        break;
                    case LEFTLOWERLEG:
                    case RIGHTLOWERARM:
                    case LEFTLOWERARM:
                    case RIGHTLOWERLEG:
                        selfPos = curSeg.pos;
                        parentCurPos = curSeg.parent.curPos;
                        parentDegree = curSeg.parent.curDegree;

                        thetaAncestor = Math.toRadians((double) parentDegree);
                        parentP = new PointF((float)(selfPos.x * Math.cos(thetaAncestor)
                                - selfPos.y * Math.sin(thetaAncestor)),
                                (float)(selfPos.y * Math.cos(thetaAncestor) + selfPos.x * Math.sin(thetaAncestor)));

                        curSeg.curPos = new PointF(parentP.x + parentCurPos.x, parentP.y + parentCurPos.y);
                        if (me.getX() >= curSeg.curPos.x) direction = -1;

                        float curDegree2 = (float) 180 - getRotationDegree(curSeg.curPos, new PointF(me.getX(), me.getY()));
                        float degree2 = (curDegree2 - curSeg.parent.degree * direction) * direction;
                        float tempDegree = degree2;

                        if (type == Config.SegType.LEFTLOWERLEG || type == Config.SegType.RIGHTLOWERLEG) {
                            if (degree2 < -Config.LEG_LIMIT) tempDegree = -Config.LEG_LIMIT;
                            if (degree2 > Config.LEG_LIMIT) tempDegree = Config.LEG_LIMIT;
                        } else {
                            if (degree2 < -Config.LOWERARM_LIMIT) tempDegree = -Config.LOWERARM_LIMIT;
                            if (degree2 > Config.LOWERARM_LIMIT) tempDegree = Config.LOWERARM_LIMIT;
                        }
                        degree2 = tempDegree;
                        curSeg.degree = degree2;
                        curSeg.curDegree = curDegree2;
                        v.invalidate();
                        break;
                    case LEFTUPPERLEG:
                    case RIGHTUPPERLEG:
                    case LEFTUPPERARM:
                    case RIGHTUPPERARM:
                        if (me.getX() >= curSeg.curPos.x) direction = -1;
                        rotateDegree = (float) 180 - getRotationDegree(curSeg.curPos, new PointF(me.getX(), me.getY())) ;

                        if (rotateDegree > Config.LEG_LIMIT && (type == Config.SegType.LEFTUPPERLEG
                                || type == Config.SegType.RIGHTUPPERLEG)) {
                            rotateDegree = Config.LEG_LIMIT;
                        }
                        curSeg.degree = rotateDegree * direction;
                        curSeg.curDegree = curSeg.degree;
                        v.invalidate();
                        break;
                }

                return true;

            } else if (me.getAction() == MotionEvent.ACTION_DOWN) {
                start = new PointF(me.getX(), me.getY());
                curSeg = torso.select(start);
                if (curSeg != null) pos = curSeg.pos;
                return true;
            }
            return false;
        }
    }

    /* Inspired by this answer from Stack Overflow: https://stackoverflow.com/a/5792353 */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            curSeg = torso.select(new PointF(scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY()));
            if ((curSeg != null) && (curSeg.type == Config.SegType.LEFTLOWERLEG
                    || curSeg.type == Config.SegType.RIGHTLOWERLEG
                    || curSeg.type == Config.SegType.LEFTUPPERLEG
                    || curSeg.type == Config.SegType.RIGHTUPPERLEG)) {
                return true;
            }
            return false;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            if (curSeg == null) return false;

            if (curSeg.type == Config.SegType.LEFTLOWERLEG || curSeg.type == Config.SegType.RIGHTLOWERLEG) {
                curSeg.scale = scaleFactor;
                curSeg.children.get(0).scale = scaleFactor;
                invalidate();
            }
            if (curSeg.type == Config.SegType.LEFTUPPERLEG || curSeg.type == Config.SegType.RIGHTUPPERLEG) {
                curSeg.scale = scaleFactor;
                curSeg.children.get(0).children.get(0).scale = scaleFactor;
                invalidate();
            }

            return true;
        }
    }
}
