package com.cash.keeper.dto;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * Ошибка
 */
public class ErrorDTO {

    /**
     * Описание ошибки
     */
    private String message;

    /**
     * Тип ошибки
     */
    private String type;

    public ErrorDTO(String message, String type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ModelAndView asModelAndView() {
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        ModelAndView modelAndView = new ModelAndView(jsonView);

        modelAndView.addObject("type", type);
        modelAndView.addObject("message", message);

        return modelAndView;
    }
}
