package com.zx.rts;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zx.rts.entity.RtCars;
import com.zx.rts.mapper.RtCarsMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public  class RtsApplicationTests {

    @Autowired
    private RtCarsMapper carsMapper;

    @Test
    public void test1() {
        QueryWrapper<RtCars> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("car_no", "皖AT305Y");
        queryWrapper.like("name","shen");
        IPage<RtCars> rtCarsIPage = carsMapper.selectPageRtCars(new Page<RtCars>(1, 1), queryWrapper);

        System.out.println(rtCarsIPage);
    }

    @Test
    public void test2() {
        QueryWrapper<RtCars> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("car_no", "皖AT305Y");
        IPage<RtCars> rtCarsIPage = carsMapper.selectPage(new Page<RtCars>(), queryWrapper);
    }


    @Test
    public void test3() {
        String content ="皖At305y";
       String pattern = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";
        System.out.println(Pattern.matches(pattern, content));

    }

    public static void main(String[] args) {


    }
}
