package com.xuyuchao.reggie.dto;
import com.xuyuchao.reggie.entity.Setmeal;
import com.xuyuchao.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
