package cn.smartGym.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.MedalMapper;
import cn.smartGym.mapper.PlayerMapper;
import cn.smartGym.pojo.Item;
import cn.smartGym.pojo.Medal;
import cn.smartGym.pojo.MedalExample;
import cn.smartGym.pojo.PlayerExample;
import cn.smartGym.pojo.PlayerExample.Criteria;
import cn.smartGym.service.CollegeService;
import cn.smartGym.service.ItemService;
import cn.smartGym.service.MedalService;
import common.utils.SGResult;

@Service
public class MedalServiceImpl implements MedalService {

	@Autowired
	private PlayerMapper playerMapper;
	@Autowired
	private MedalMapper medalMapper;
	@Autowired
	private CollegeService collegeService;
	@Autowired
	private ItemService itemService;

	@Override
	public SGResult updateByGame(String game) {
		Item item = new Item();
		item.setGame(game);
		List<Item> items = itemService.getItemsByDetailsAndStatuses(item);
		List<Long> itemIds = itemService.getItemIdsByItems(items);

		Map<Integer, String> allCollegeIdsAndName = collegeService.getAllCollegeIdsAndName();
		Set<Integer> collegeIds = allCollegeIdsAndName.keySet();
		for (Integer collegeId : collegeIds) {
			Medal medal = new Medal();
			medal.setGame(game);
			medal.setCollege(collegeId);
			medal.setStatus(1);
			medal.setUpdated(new Date());
			for (int i = 1; i <= 8; i++) {
				PlayerExample playerExample = new PlayerExample();
				Criteria criteria = playerExample.createCriteria();
				criteria.andCollegeEqualTo(collegeId);
				criteria.andItemIdIn(itemIds);
				criteria.andStatusNotEqualTo(0);
				criteria.andRankNoIsNotNull();
				criteria.andRankNoLessThanOrEqualTo(i);
				Long countByExample = playerMapper.countByExample(playerExample);
				switch (i) {
				case 1:
					medal.setFirst(countByExample.intValue());
					break;
				case 2:
					medal.setSecond(countByExample.intValue());
					break;
				case 3:
					medal.setThird(countByExample.intValue());
					break;
				case 4:
					medal.setFourth(countByExample.intValue());
					break;
				case 5:
					medal.setFifth(countByExample.intValue());
					break;
				case 6:
					medal.setSixth(countByExample.intValue());
					break;
				case 7:
					medal.setSeventh(countByExample.intValue());
					break;
				case 8:
					medal.setEighth(countByExample.intValue());
					break;
				default:
					break;
				}
			}
			MedalExample medalExample = new MedalExample();
			cn.smartGym.pojo.MedalExample.Criteria medalCriteria = medalExample.createCriteria();
			medalCriteria.andCollegeEqualTo(collegeId);
			medalCriteria.andGameEqualTo(game);
			medalCriteria.andStatusNotEqualTo(0);
			List<Medal> selectByExample = medalMapper.selectByExample(medalExample);
			if (selectByExample == null || selectByExample.isEmpty()) {
				medal.setCreated(new Date());
				medalMapper.insertSelective(medal);
			} else {
				medal.setId(selectByExample.get(0).getId());
				medalMapper.updateByPrimaryKeySelective(medal);
			}
		}
		return SGResult.ok("更新奖牌榜成功！");
	}

	@Override
	public SGResult updateByGameAndCollege(String game, Integer collegeId) {
		Item item = new Item();
		item.setGame(game);
		List<Item> items = itemService.getItemsByDetailsAndStatuses(item);
		List<Long> itemIds = itemService.getItemIdsByItems(items);

		Medal medal = new Medal();
		medal.setGame(game);
		medal.setCollege(collegeId);
		medal.setUpdated(new Date());
		medal.setStatus(1);
		for (int i = 1; i <= 8; i++) {
			PlayerExample playerExample = new PlayerExample();
			Criteria criteria = playerExample.createCriteria();
			criteria.andCollegeEqualTo(collegeId);
			criteria.andItemIdIn(itemIds);
			criteria.andStatusNotEqualTo(0);
			criteria.andRankNoIsNotNull();
			criteria.andRankNoLessThanOrEqualTo(i);
			Long countByExample = playerMapper.countByExample(playerExample);
			switch (i) {
			case 1:
				medal.setFirst(countByExample.intValue());
				break;
			case 2:
				medal.setSecond(countByExample.intValue());
				break;
			case 3:
				medal.setThird(countByExample.intValue());
				break;
			case 4:
				medal.setFourth(countByExample.intValue());
				break;
			case 5:
				medal.setFifth(countByExample.intValue());
				break;
			case 6:
				medal.setSixth(countByExample.intValue());
				break;
			case 7:
				medal.setSeventh(countByExample.intValue());
				break;
			case 8:
				medal.setEighth(countByExample.intValue());
				break;
			default:
				break;
			}
		}
		MedalExample medalExample = new MedalExample();
		cn.smartGym.pojo.MedalExample.Criteria medalCriteria = medalExample.createCriteria();
		medalCriteria.andCollegeEqualTo(collegeId);
		medalCriteria.andGameEqualTo(game);
		medalCriteria.andStatusNotEqualTo(0);
		List<Medal> selectByExample = medalMapper.selectByExample(medalExample);
		if (selectByExample == null || selectByExample.isEmpty()) {
			medal.setCreated(new Date());
			medalMapper.insertSelective(medal);
		} else {
			medal.setId(selectByExample.get(0).getId());
			medalMapper.updateByPrimaryKeySelective(medal);
		}

		return SGResult.ok("更新奖牌榜成功！");
	}

	@Override
	public void hardDeleteMedal() {
		MedalExample medalExample = new MedalExample();
		cn.smartGym.pojo.MedalExample.Criteria medalCriteria = medalExample.createCriteria();
		medalCriteria.andStatusEqualTo(0);
		medalMapper.deleteByExample(medalExample);
	}

	@Override
	public void maintenanceMedal() {
		updateByGame(null);
	}

	@Override
	public List<Medal> getMedalsByDetails(Medal medal) {
		MedalExample medalExample = new MedalExample();
		cn.smartGym.pojo.MedalExample.Criteria medalCriteria = medalExample.createCriteria();
		if(medal != null) {
			if (medal.getCollege() != null)
				medalCriteria.andCollegeEqualTo(medal.getCollege());
			if(StringUtils.isNotBlank(medal.getGame()))
				medalCriteria.andGameEqualTo(medal.getGame());
		}		
		medalCriteria.andStatusNotEqualTo(0);
		List<Medal> medals = medalMapper.selectByExample(medalExample);
		return medals;
	}

}
