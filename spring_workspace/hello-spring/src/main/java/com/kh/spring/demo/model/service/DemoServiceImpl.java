package com.kh.spring.demo.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.demo.model.dao.DemoDao;
import com.kh.spring.demo.model.vo.Dev;

@Service
public class DemoServiceImpl implements DemoService{

	@Autowired
	private DemoDao demoDao;

	@Override
	public int insertDev(Dev dev) {
		//1. SqlSession 객체 생성
		//2. dao 업무요청
		//3. 트랜잭션 처리(DML)
		//4. SqlSession 반납
		
		// AOP(관점 지향 프로그래밍)을 사용하기 때문에,
		// 실질적으로 프로그래머는 2번만 작성하면 된다.
		// SqlSession 객체 넘기지 않아도 Dao에서 알아서 가져다가 씀.
		int result = demoDao.insertDev(dev);
		return result;
	}

	@Override
	public List<Dev> selectDevList() {
		//1. SqlSession 객체 생성
		//2. dao 업무요청
//		List<Dev> list = demoDao.selectDevList();
		//3. 트랜잭션 처리(DML)		
		//4. SqlSession 반납
//		return list;
		return demoDao.selectDevList();
	}

	@Override
	public Dev selectDevOne(int no) {
		return demoDao.selectDevOne(no);
	}
	
	/**
	 * transaction처리(AOP)
	 * 예외가 발생하지 않으면 commit
	 * 예외가 발생하면 rollback
	 */
	@Override
	public int updateDevOne(Dev dev) {
		return demoDao.updateDevOne(dev);
	}

	@Override
	public int deleteDevOne(int no) {
		return demoDao.deleteDevOne(no);
	}
	
}
