package com.sogou.upd.passport.manager.form;

import com.google.common.base.Strings;
import com.sogou.upd.passport.common.utils.ProvinceAndCityUtil;
import com.sogou.upd.passport.common.validation.constraints.Birthday;
import com.sogou.upd.passport.common.validation.constraints.Gender;
import com.sogou.upd.passport.common.validation.constraints.IdCard;
import com.sogou.upd.passport.common.validation.constraints.IllegalSensitive;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;

/**
 * User: mayan
 * Date: 13-8-8 Time: 下午2:18
 */
public class AccountInfoParams {
    @NotBlank(message = "client_id不允许为空!")
    @Min(0)
    private String client_id;
    private String username;
    @Birthday
    private String birthday;  //用户生日
    @Gender
    private String gender;  //用户性别
    private String province;  //省份
    private String city;    //城市
    @IllegalSensitive
    private String fullname;  //姓名
    @IdCard
    private String personalid;     //身份证号
    @IllegalSensitive
    private String uniqname;

    @AssertTrue(message = "省市参数错误！")
    public boolean isCheckProvinceAndCity() {
        if (StringUtils.isNotEmpty(province)) {
            if (Strings.isNullOrEmpty(ProvinceAndCityUtil.immutableProvinceMap.get(province))) {
                return false;
            }
        }
        if (StringUtils.isNotEmpty(city)) {
            if (Strings.isNullOrEmpty(ProvinceAndCityUtil.immutableCityMap.get(city))) {
                return false;
            }
        }
        if (StringUtils.isNotEmpty(province) && StringUtils.isNotEmpty(city)) {
            if (!StringUtils.substring(province, 0, 2).equalsIgnoreCase(StringUtils.substring(city, 0, 2))) {
                return false;
            }
        }
        return true;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUniqname() {
        return uniqname;
    }

    public void setUniqname(String uniqname) {
        this.uniqname = uniqname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPersonalid() {
        return personalid;
    }

    public void setPersonalid(String personalid) {
        this.personalid = personalid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

}
