/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.pz.sinaweibosample.model.entity;

import com.example.pz.sinaweibosample.base.BaseObject;


/**
 * 评论结构体。
 * 
 * @author SINA
 * @since 2013-11-24
 */
public class Comment extends BaseObject{

    /** 评论创建时间 */
    private String created_at;
    /** 评论的 ID */
    private String id;
    /** 评论的内容 */
    private String text;
    /** 评论的来源 */
    private String source;
    /** 评论作者的用户信息字段 */
    private User user;
    /** 评论的 MID */
    private String mid;
    /** 字符串型的评论 ID */
    private String idstr;
    /** 评论的微博信息字段 */
    private Status status;
    /** 评论来源评论，当本评论属于对另一评论的回复时返回此字段 */
    private Comment reply_comment;

    public String toString() {
        return text + "  评论：" + status.getText();
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Comment getReply_comment() {
        return reply_comment;
    }

    public void setReply_comment(Comment reply_comment) {
        this.reply_comment = reply_comment;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
