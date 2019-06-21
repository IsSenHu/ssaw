package com.ssaw.bht.node.abs;

import com.ssaw.bht.cons.EStatus;
import com.ssaw.bht.node.Node;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HuSen
 * create on 2019/6/21 16:49
 */
@Setter
public abstract class BaseNode implements Node {

    private String id;

    private Map<String, Node> children;

    private Integer order;

    private EStatus status;

    BaseNode(String id) {
        this.id = id;
        this.order = judgeOrder(id);
        this.children = new HashMap<>(16);
        this.status = EStatus.Initial;
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public EStatus getStatus() {
        return this.status;
    }

    @Override
    public void addChild(Node node) {
        Integer targetOrder = judgeOrder(node.id());
        // 这是我的父亲
        if (targetOrder - order == 1) {
            children.put(node.id(), node);
        } else {
            String parentId = getParentId(node.id());
            String[] ids = new String[targetOrder];
            int tempOrder;
            while ((tempOrder = judgeOrder(parentId)) > this.order) {
                ids[tempOrder] = parentId;
                parentId = getParentId(parentId);
            }
            Node targetParent = null;
            for (int i = this.order + 1; i < targetOrder; i++) {
                targetParent = getChild(ids[i]);
            }
            if (targetParent != null) {
                targetParent.addChild(node);
            }
        }
    }

    @Override
    public Node getChild(String id) {
        return this.children.get(id);
    }

    @Override
    public List<Node> children() {
        return new ArrayList<>(this.children.values());
    }

    @Override
    public EStatus run() {
        if (!EStatus.Running.is(status)) {
            onInitialize();
        }
        status = exec();
        if (!EStatus.Running.is(status)) {
            onTerminate(status);
        }
        return status;
    }

    @Override
    public void onInitialize() {}

    @Override
    public void onTerminate(EStatus status) {}

    private String getParentId(String id) {
        return StringUtils.substringBeforeLast(id, "-");
    }

    private Integer judgeOrder(String id) {
        return StringUtils.split(id, "-").length;
    }
}
