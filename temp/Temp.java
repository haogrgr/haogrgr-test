package com.haogrgr.test.main;

import java.util.Date;
import java.util.List;

import org.springframework.util.Assert;

import com.haogrgr.test.model.UserModel;
import com.haogrgr.test.model.UserPhoneKey;
import com.haogrgr.test.model.UserPhoneModel;
import com.haogrgr.test.pojo.PageInfo;
import com.haogrgr.test.server.UserPhoneService;
import com.haogrgr.test.server.UserService;
import com.haogrgr.test.util.Lists;
import com.haogrgr.test.util.TestUtils;

public class Temp {

	public static void main(String[] args) throws Exception {
		TestUtils.initSpring();

		UserService userMapper = TestUtils.getBean(UserService.class);
		testUserMapper(userMapper);

		UserPhoneService userPhoneMapper = TestUtils.getBean(UserPhoneService.class);
		testUserPhoneMapper(userPhoneMapper);

	}

	public static void testUserMapper(UserService userMapper) {
		{
			UserModel user1 = new UserModel();
			user1.setUserName("user1");
			user1.setUserAge(1);
			user1.setModifyTime(new Date());
			user1.setCreateTime(new Date());
			Integer save = userMapper.save(user1);
			Assert.isTrue(save.intValue() == 1);
			Assert.isTrue(user1.getUserId().intValue() == 1);

			UserModel byId = userMapper.getById(1);
			Assert.isTrue(byId.getUserName().equals(user1.getUserName()));
		}

		{
			List<UserModel> users = Lists.array();
			for (int i = 2; i < 21; i++) {
				UserModel user = new UserModel();
				user.setUserName("user" + i);
				user.setUserAge(i);
				user.setModifyTime(new Date());
				user.setCreateTime(new Date());
				users.add(user);
			}
			Integer saveBatch = userMapper.saveBatch(users);
			Assert.isTrue(saveBatch.intValue() == 19);
		}

		{
			List<UserModel> all = userMapper.getAll();
			Assert.isTrue(all.size() == 20);
			Assert.isTrue(all.get(19).getUserId() == 20);

			Integer allCount = userMapper.getAllCount();
			Assert.isTrue(allCount.intValue() == 20);

			List<UserModel> byIds = userMapper.getByIds(Lists.array(1, 2, 100));
			Assert.isTrue(byIds.size() == 2);
			Assert.isTrue(byIds.get(1).getUserId() == 2);
		}

		{
			UserModel pojo = new UserModel();
			pojo.setUserAge(11);
			pojo.setUserName("user11");
			List<UserModel> byPojo = userMapper.getByPojo(pojo);
			Assert.isTrue(byPojo.size() == 1 && byPojo.get(0).getUserId() == 11);

			pojo.setUserName("xxx");
			byPojo = userMapper.getByPojo(pojo);
			Assert.isTrue(byPojo.size() == 0);

			pojo = new UserModel();
			byPojo = userMapper.getByPojo(pojo);
			Assert.isTrue(byPojo.size() == 20);
		}
		{
			UserModel record = new UserModel();
			record.setUserId(1);
			record.setUserAge(11111);
			Integer modify = userMapper.modify(record);
			Assert.isTrue(modify.intValue() == 1);

			UserModel byId = userMapper.getById(1);
			Assert.isTrue(byId.getUserName() == null && byId.getUserAge().intValue() == 11111);

			record = new UserModel();
			record.setUserId(2);
			record.setUserAge(22222);
			Integer modefySelective = userMapper.modifySelective(record);
			Assert.isTrue(modefySelective.intValue() == 1);

			byId = userMapper.getById(2);
			Assert.isTrue(byId.getUserName() != null && byId.getUserAge().intValue() == 22222);
		}

		{
			Integer delById = userMapper.delById(1);
			Assert.isTrue(delById.intValue() == 1);
			Assert.isTrue(userMapper.getById(1) == null);

			Integer delByIds = userMapper.delByIds(Lists.array(1, 2, 3));
			Assert.isTrue(delByIds.intValue() == 2);
			Assert.isTrue(userMapper.getById(2) == null);
			Assert.isTrue(userMapper.getById(3) == null);
			Assert.isTrue(userMapper.getAllCount().intValue() == 17);
		}

		{
			PageInfo<UserModel> page = new PageInfo<>(1, 10);
			List<UserModel> byPageList = userMapper.getByPageList(page);
			Assert.isTrue(byPageList.size() == 10);
			Integer byPageCount = userMapper.getByPageCount(page);
			Assert.isTrue(byPageCount.intValue() == 17);

			page = new PageInfo<>(2, 10);
			byPageList = userMapper.getByPageList(page);
			Assert.isTrue(byPageList.size() == 7);
			byPageCount = userMapper.getByPageCount(page);
			Assert.isTrue(byPageCount.intValue() == 17);

			page = new PageInfo<>(1, 10);
			page.addParam("userName", "user4");
			page.addParam("userAge", "4");
			byPageList = userMapper.getByPageList(page);
			Assert.isTrue(byPageList.size() == 1);
			byPageCount = userMapper.getByPageCount(page);
			Assert.isTrue(byPageCount.intValue() == 1);

			page = new PageInfo<>(1, 10);
			page.addParam("userName", "user4");
			page.addParam("userAge", "5");
			byPageList = userMapper.getByPageList(page);
			Assert.isTrue(byPageList.size() == 0);
			byPageCount = userMapper.getByPageCount(page);
			Assert.isTrue(byPageCount.intValue() == 0);
		}

	}

	public static void testUserPhoneMapper(UserPhoneService userPhoneMapper) {
		{
			UserPhoneModel userPhone = new UserPhoneModel();
			userPhone.setUserId(1);
			userPhone.setPhone("1");
			userPhone.setSalt("1");
			userPhone.setModifyTime(new Date());
			userPhone.setCreateTime(new Date());
			Integer save = userPhoneMapper.save(userPhone);
			Assert.isTrue(save.intValue() == 1);

			UserPhoneModel byId = userPhoneMapper.getById(new UserPhoneKey(1, "1"));
			Assert.isTrue(byId.getSalt().equals(userPhone.getSalt()));
		}

		{
			List<UserPhoneModel> userPhones = Lists.array();
			for (int i = 2; i < 21; i++) {
				UserPhoneModel userPhone = new UserPhoneModel();
				userPhone.setUserId(i);
				userPhone.setPhone("" + i);
				userPhone.setSalt("" + i);
				userPhone.setModifyTime(new Date());
				userPhone.setCreateTime(new Date());
				userPhones.add(userPhone);
			}
			Integer saveBatch = userPhoneMapper.saveBatch(userPhones);
			Assert.isTrue(saveBatch.intValue() == 19);
		}

		{
			try {
				userPhoneMapper.getByIds(Lists.array(new UserPhoneKey(1, "1")));
				Assert.isTrue(false);
			} catch (Exception e) {
				//mast exp
			}
		}

	}

}
