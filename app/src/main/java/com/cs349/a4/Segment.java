package com.cs349.a4;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;


public class Segment {

    Config.SegType type;
    RectF rectf;
    Segment parent;
    List<Segment> children = new ArrayList<>();
    PointF pos;
    PointF curPos;
    Bitmap image;
    float degree = 0;
    float curDegree = 0;
    float scale = 1;
    float imageLeft;
    float imageTop;

    public Segment(RectF rectf, Config.SegType type) {
        this.rectf = rectf;
        this.type = type;
    }

    public Segment select(PointF mouse) {
        float pX = mouse.x - pos.x;
        float pY = mouse.y - pos.y;
        float adjustedScaler = (scale - 1) / 4 + 1;
        double theta = Math.toRadians((double) -1 * degree);

        float mappedX = (int)(pX * Math.cos(theta) - pY * Math.sin(theta));
        float mappedY = (int)((pY * Math.cos(theta) + pX * Math.sin(theta)) / adjustedScaler);

        PointF testP = new PointF(mappedX, mappedY);
        if (rectf.contains(mappedX, mappedY)) return this;

        for (Segment seg: children) {
            Segment selected = seg.select(testP);
            if (selected != null) return selected;
        }
        return null;
    }

    public void drawSegment(Canvas c) {
        float s = (scale-1)/4+1;
        float s_feet = 1/s;
        c.save();
        c.translate(pos.x, pos.y);
        c.rotate(degree);

        if (type != Config.SegType.FEET) {
            c.scale(1, s);
        } else {
            c.scale(1, s_feet);
        }

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        if (image != null) {
            c.drawBitmap(image, imageLeft, imageTop, paint);
        }
        c.scale(1, 1);

        for(Segment seg : children) {
            seg.drawSegment(c);
            c.restore();
        }
    }

    public void addChild(Segment seg, PointF pos){
        seg.pos = pos;
        seg.curPos = new PointF(pos.x + curPos.x, pos.y + curPos.y);
        seg.parent = this;
        this.children.add(seg);
    }
}
