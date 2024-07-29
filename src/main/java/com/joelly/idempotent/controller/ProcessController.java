package com.joelly.idempotent.controller;


import com.joelly.idempotent.switchs.IdempotentProcessSwitch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/joelly/idempotent")
public class ProcessController {

    @GetMapping("/hello")
    public String hello() {
        return "ok";
    }

    /**
     * 幂等拦截总开关
     *
     * @param switchValue: true=开启拦截，false=关闭拦截
     * @return
     */
    @PostMapping("/switch/preAspectProcessSwitch")
    public String updatePreAspectProcessSwitch(boolean switchValue) {
        IdempotentProcessSwitch.setPreAspectProcessSwitch(switchValue);
        return "OK";
    }
}