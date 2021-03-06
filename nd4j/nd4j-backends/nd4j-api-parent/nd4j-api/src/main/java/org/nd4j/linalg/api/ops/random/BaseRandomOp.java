package org.nd4j.linalg.api.ops.random;

import lombok.NoArgsConstructor;
import lombok.val;
import org.nd4j.autodiff.samediff.SDVariable;
import org.nd4j.autodiff.samediff.SameDiff;
import org.nd4j.base.Preconditions;
import org.nd4j.linalg.api.ops.BaseOp;
import org.nd4j.linalg.api.ops.RandomOp;
import org.nd4j.linalg.api.shape.Shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author raver119@gmail.com
 */
@NoArgsConstructor
public abstract class BaseRandomOp extends BaseOp implements RandomOp {
    protected long[] shape;

    public BaseRandomOp(SameDiff sameDiff, SDVariable i_v) {
        Preconditions.checkNotNull(i_v, "Input variable can't be null with this constructor");
        this.sameDiff = sameDiff;
        this.xVertexId = i_v.getVarName();
        sameDiff.addArgsFor(new String[]{xVertexId},this);
        if(Shape.isPlaceholderShape(i_v.getShape())) {
            sameDiff.addPropertyToResolve(this,i_v.getVarName());
        }
    }

    public BaseRandomOp(SameDiff sd, long[] shape){
        super(sd, null);
        Preconditions.checkArgument(shape != null && shape.length > 0, "Shape must be non-null, length > 0. Got: %s", shape);
        this.sameDiff = sd;
        this.shape = shape;
        setInstanceId();
        sameDiff.addArgsFor(new String[0], this);
    }

    @Override
    public Type opType() {
        return Type.RANDOM;
    }

    @Override
    public List<long[]> calculateOutputShape() {
        if(shape != null){
            return Collections.singletonList(shape);
        } else {
            List<long[]> ret = new ArrayList<>(1);
            val shape = sameDiff.getShapeForVarName(args()[0].getVarName());
            if (shape != null)
                ret.add(shape);
            return ret;
        }
    }
}
