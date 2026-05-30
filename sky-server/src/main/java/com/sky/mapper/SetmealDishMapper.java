package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据套餐id 查询对应的菜品id
     * @param setmealId
     * @return
     */
    //@Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<Long> getSetmealIdsByDishIds(List<Long> setmealId);

    /**
     * 批量插入菜品数据
     * @param dishes
     */
    void insertBatch(List<SetmealDish> dishes);

    /**
     * 根据套餐id删除套餐和菜品的关联关系
     * @param setmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    /**
     * 根据套餐id查询套餐和菜品的关联关系
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);

    /**
     * 查询包含当前菜品的套餐id
     * @param id
     * @return
     */
    List<Long> getSetmealIdsByDishId(Long id);

    /**
     *
     * @param status
     * @param ids
     */
    void updateStatusByIds(@Param("status") Integer status,
                           @Param("ids") List<Long> ids);
}
