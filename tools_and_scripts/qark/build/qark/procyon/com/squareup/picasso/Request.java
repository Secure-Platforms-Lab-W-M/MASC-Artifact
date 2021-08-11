// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import android.net.Uri;
import java.util.List;
import android.graphics.Bitmap$Config;

public final class Request
{
    private static final long TOO_LONG_LOG;
    public final boolean centerCrop;
    public final boolean centerInside;
    public final Bitmap$Config config;
    public final boolean hasRotationPivot;
    int id;
    int networkPolicy;
    public final boolean onlyScaleDown;
    public final Picasso.Priority priority;
    public final int resourceId;
    public final float rotationDegrees;
    public final float rotationPivotX;
    public final float rotationPivotY;
    public final String stableKey;
    long started;
    public final int targetHeight;
    public final int targetWidth;
    public final List<Transformation> transformations;
    public final Uri uri;
    
    static {
        TOO_LONG_LOG = TimeUnit.SECONDS.toNanos(5L);
    }
    
    private Request(final Uri uri, final int resourceId, final String stableKey, final List<Transformation> list, final int targetWidth, final int targetHeight, final boolean centerCrop, final boolean centerInside, final boolean onlyScaleDown, final float rotationDegrees, final float rotationPivotX, final float rotationPivotY, final boolean hasRotationPivot, final Bitmap$Config config, final Picasso.Priority priority) {
        this.uri = uri;
        this.resourceId = resourceId;
        this.stableKey = stableKey;
        if (list == null) {
            this.transformations = null;
        }
        else {
            this.transformations = Collections.unmodifiableList((List<? extends Transformation>)list);
        }
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        this.centerCrop = centerCrop;
        this.centerInside = centerInside;
        this.onlyScaleDown = onlyScaleDown;
        this.rotationDegrees = rotationDegrees;
        this.rotationPivotX = rotationPivotX;
        this.rotationPivotY = rotationPivotY;
        this.hasRotationPivot = hasRotationPivot;
        this.config = config;
        this.priority = priority;
    }
    
    public Builder buildUpon() {
        return new Builder(this);
    }
    
    String getName() {
        if (this.uri != null) {
            return String.valueOf(this.uri.getPath());
        }
        return Integer.toHexString(this.resourceId);
    }
    
    boolean hasCustomTransformations() {
        return this.transformations != null;
    }
    
    public boolean hasSize() {
        return this.targetWidth != 0 || this.targetHeight != 0;
    }
    
    String logId() {
        final long n = System.nanoTime() - this.started;
        if (n > Request.TOO_LONG_LOG) {
            return this.plainId() + '+' + TimeUnit.NANOSECONDS.toSeconds(n) + 's';
        }
        return this.plainId() + '+' + TimeUnit.NANOSECONDS.toMillis(n) + "ms";
    }
    
    boolean needsMatrixTransform() {
        return this.hasSize() || this.rotationDegrees != 0.0f;
    }
    
    boolean needsTransformation() {
        return this.needsMatrixTransform() || this.hasCustomTransformations();
    }
    
    String plainId() {
        return "[R" + this.id + ']';
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Request{");
        if (this.resourceId > 0) {
            sb.append(this.resourceId);
        }
        else {
            sb.append(this.uri);
        }
        if (this.transformations != null && !this.transformations.isEmpty()) {
            final Iterator<Transformation> iterator = this.transformations.iterator();
            while (iterator.hasNext()) {
                sb.append(' ').append(iterator.next().key());
            }
        }
        if (this.stableKey != null) {
            sb.append(" stableKey(").append(this.stableKey).append(')');
        }
        if (this.targetWidth > 0) {
            sb.append(" resize(").append(this.targetWidth).append(',').append(this.targetHeight).append(')');
        }
        if (this.centerCrop) {
            sb.append(" centerCrop");
        }
        if (this.centerInside) {
            sb.append(" centerInside");
        }
        if (this.rotationDegrees != 0.0f) {
            sb.append(" rotation(").append(this.rotationDegrees);
            if (this.hasRotationPivot) {
                sb.append(" @ ").append(this.rotationPivotX).append(',').append(this.rotationPivotY);
            }
            sb.append(')');
        }
        if (this.config != null) {
            sb.append(' ').append(this.config);
        }
        sb.append('}');
        return sb.toString();
    }
    
    public static final class Builder
    {
        private boolean centerCrop;
        private boolean centerInside;
        private Bitmap$Config config;
        private boolean hasRotationPivot;
        private boolean onlyScaleDown;
        private Picasso.Priority priority;
        private int resourceId;
        private float rotationDegrees;
        private float rotationPivotX;
        private float rotationPivotY;
        private String stableKey;
        private int targetHeight;
        private int targetWidth;
        private List<Transformation> transformations;
        private Uri uri;
        
        public Builder(final int resourceId) {
            this.setResourceId(resourceId);
        }
        
        public Builder(final Uri uri) {
            this.setUri(uri);
        }
        
        Builder(final Uri uri, final int resourceId, final Bitmap$Config config) {
            this.uri = uri;
            this.resourceId = resourceId;
            this.config = config;
        }
        
        private Builder(final Request request) {
            this.uri = request.uri;
            this.resourceId = request.resourceId;
            this.stableKey = request.stableKey;
            this.targetWidth = request.targetWidth;
            this.targetHeight = request.targetHeight;
            this.centerCrop = request.centerCrop;
            this.centerInside = request.centerInside;
            this.rotationDegrees = request.rotationDegrees;
            this.rotationPivotX = request.rotationPivotX;
            this.rotationPivotY = request.rotationPivotY;
            this.hasRotationPivot = request.hasRotationPivot;
            this.onlyScaleDown = request.onlyScaleDown;
            if (request.transformations != null) {
                this.transformations = new ArrayList<Transformation>(request.transformations);
            }
            this.config = request.config;
            this.priority = request.priority;
        }
        
        public Request build() {
            if (this.centerInside && this.centerCrop) {
                throw new IllegalStateException("Center crop and center inside can not be used together.");
            }
            if (this.centerCrop && this.targetWidth == 0 && this.targetHeight == 0) {
                throw new IllegalStateException("Center crop requires calling resize with positive width and height.");
            }
            if (this.centerInside && this.targetWidth == 0 && this.targetHeight == 0) {
                throw new IllegalStateException("Center inside requires calling resize with positive width and height.");
            }
            if (this.priority == null) {
                this.priority = Picasso.Priority.NORMAL;
            }
            return new Request(this.uri, this.resourceId, this.stableKey, this.transformations, this.targetWidth, this.targetHeight, this.centerCrop, this.centerInside, this.onlyScaleDown, this.rotationDegrees, this.rotationPivotX, this.rotationPivotY, this.hasRotationPivot, this.config, this.priority, null);
        }
        
        public Builder centerCrop() {
            if (this.centerInside) {
                throw new IllegalStateException("Center crop can not be used after calling centerInside");
            }
            this.centerCrop = true;
            return this;
        }
        
        public Builder centerInside() {
            if (this.centerCrop) {
                throw new IllegalStateException("Center inside can not be used after calling centerCrop");
            }
            this.centerInside = true;
            return this;
        }
        
        public Builder clearCenterCrop() {
            this.centerCrop = false;
            return this;
        }
        
        public Builder clearCenterInside() {
            this.centerInside = false;
            return this;
        }
        
        public Builder clearOnlyScaleDown() {
            this.onlyScaleDown = false;
            return this;
        }
        
        public Builder clearResize() {
            this.targetWidth = 0;
            this.targetHeight = 0;
            this.centerCrop = false;
            this.centerInside = false;
            return this;
        }
        
        public Builder clearRotation() {
            this.rotationDegrees = 0.0f;
            this.rotationPivotX = 0.0f;
            this.rotationPivotY = 0.0f;
            this.hasRotationPivot = false;
            return this;
        }
        
        public Builder config(final Bitmap$Config config) {
            this.config = config;
            return this;
        }
        
        boolean hasImage() {
            return this.uri != null || this.resourceId != 0;
        }
        
        boolean hasPriority() {
            return this.priority != null;
        }
        
        boolean hasSize() {
            return this.targetWidth != 0 || this.targetHeight != 0;
        }
        
        public Builder onlyScaleDown() {
            if (this.targetHeight == 0 && this.targetWidth == 0) {
                throw new IllegalStateException("onlyScaleDown can not be applied without resize");
            }
            this.onlyScaleDown = true;
            return this;
        }
        
        public Builder priority(final Picasso.Priority priority) {
            if (priority == null) {
                throw new IllegalArgumentException("Priority invalid.");
            }
            if (this.priority != null) {
                throw new IllegalStateException("Priority already set.");
            }
            this.priority = priority;
            return this;
        }
        
        public Builder resize(final int targetWidth, final int targetHeight) {
            if (targetWidth < 0) {
                throw new IllegalArgumentException("Width must be positive number or 0.");
            }
            if (targetHeight < 0) {
                throw new IllegalArgumentException("Height must be positive number or 0.");
            }
            if (targetHeight == 0 && targetWidth == 0) {
                throw new IllegalArgumentException("At least one dimension has to be positive number.");
            }
            this.targetWidth = targetWidth;
            this.targetHeight = targetHeight;
            return this;
        }
        
        public Builder rotate(final float rotationDegrees) {
            this.rotationDegrees = rotationDegrees;
            return this;
        }
        
        public Builder rotate(final float rotationDegrees, final float rotationPivotX, final float rotationPivotY) {
            this.rotationDegrees = rotationDegrees;
            this.rotationPivotX = rotationPivotX;
            this.rotationPivotY = rotationPivotY;
            this.hasRotationPivot = true;
            return this;
        }
        
        public Builder setResourceId(final int resourceId) {
            if (resourceId == 0) {
                throw new IllegalArgumentException("Image resource ID may not be 0.");
            }
            this.resourceId = resourceId;
            this.uri = null;
            return this;
        }
        
        public Builder setUri(final Uri uri) {
            if (uri == null) {
                throw new IllegalArgumentException("Image URI may not be null.");
            }
            this.uri = uri;
            this.resourceId = 0;
            return this;
        }
        
        public Builder stableKey(final String stableKey) {
            this.stableKey = stableKey;
            return this;
        }
        
        public Builder transform(final Transformation transformation) {
            if (transformation == null) {
                throw new IllegalArgumentException("Transformation must not be null.");
            }
            if (transformation.key() == null) {
                throw new IllegalArgumentException("Transformation key must not be null.");
            }
            if (this.transformations == null) {
                this.transformations = new ArrayList<Transformation>(2);
            }
            this.transformations.add(transformation);
            return this;
        }
        
        public Builder transform(final List<? extends Transformation> list) {
            if (list == null) {
                throw new IllegalArgumentException("Transformation list must not be null.");
            }
            for (int i = 0; i < list.size(); ++i) {
                this.transform((Transformation)list.get(i));
            }
            return this;
        }
    }
}
