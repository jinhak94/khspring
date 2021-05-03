package com.kh.spring.menu.model.dao;

import java.util.List;

import com.kh.spring.menu.model.vo.Menu;

public interface MenuDao {

	List<Menu> selectMenuList();

	List<Menu> selectTypeMenuList(String type);

	int insertMenu(Menu menu);

	Menu selectMenu(int id);

	int updateMenu(Menu menu);

	int deleteMenu(int id);

}
