package com.xc.springbootpractice.Entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Component
@ConfigurationProperties(prefix = "student")
public class Student {

    @Id
    @GeneratedValue                     //主键生成策略为自增长
    private Integer id;

    @NotNull(message = "名字不能为空")
    private String name;

    @Min(value = 18,message = "未成年")
    private Integer age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
