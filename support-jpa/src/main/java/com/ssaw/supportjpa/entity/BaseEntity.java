package com.ssaw.supportjpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen.
 * @date 2018/12/12 10:33.
 */
@MappedSuperclass
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -4738420985478390653L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime createTime;

    @Column
    private LocalDateTime modifyTime;

    @Column
    private String createMan;

    @Column
    private String modifyMan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public String getModifyMan() {
        return modifyMan;
    }

    public void setModifyMan(String modifyMan) {
        this.modifyMan = modifyMan;
    }
}
