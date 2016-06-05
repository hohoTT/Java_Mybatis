package wt.seckill.web;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import wt.seckill.dto.Exposer;
import wt.seckill.dto.SeckillExecution;
import wt.seckill.dto.SeckillResult;
import wt.seckill.entity.Seckill;
import wt.seckill.enums.SeckillStatEnum;
import wt.seckill.exception.RepeatKillException;
import wt.seckill.exception.SeckillCloseException;
import wt.seckill.service.SeckillService;

// 将目前的 SeckillController 放入spring容器中
@Controller
@RequestMapping("/seckill")
// url: /模块/资源/{id}/细分 /seckill/list
public class SeckillController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SeckillService seckillService;

	// 获取秒杀的列表页
	// Model 用来存放渲染页面的数据
	// list.jsp 为页面的模板
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		// 获取列表页
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list", list);

		// list.jsp + model = ModelAndView

		return "list"; // /WEB-INF/jsp/"list".jsp
	}

	// 详情页
	@RequestMapping(value = "{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model) {

		if (seckillId == null) {
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.getById(seckillId);
		if (seckill == null) {
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill", seckill);

		return "detail";
	}

	// ajax 接口，json
	// @ResponseBody 告诉springMVC 该方法返回的为 json 数据
	@RequestMapping(value = "{seckillId}/exposer", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public SeckillResult<Exposer> exposer(Long seckillId) {

		SeckillResult<Exposer> result;

		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = new SeckillResult<Exposer>(false, e.getMessage());
		}
		return result;

	}

	// 秒杀执行方法
	@RequestMapping(value = "{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public SeckillResult<SeckillExecution> execute(
			@PathVariable("seckillId") Long seckillId,
			@CookieValue(value = "userPhone", required = false) Long userPhone,
			@PathVariable("md5") String md5) {

		// springMVC valid
		if (userPhone == null) {
			return new SeckillResult<SeckillExecution>(false, "未注册");
		}
		try {
			SeckillExecution seckillExecution = seckillService.executeSeckill(
					seckillId, userPhone, md5);
			return new SeckillResult<SeckillExecution>(true, seckillExecution);
		} catch (RepeatKillException e) {
			SeckillExecution seckillExecution = new SeckillExecution(seckillId,
					SeckillStatEnum.REPEAT_KILL);
			return new SeckillResult<SeckillExecution>(false, seckillExecution);
		} catch (SeckillCloseException e) {
			SeckillExecution seckillExecution = new SeckillExecution(seckillId,
					SeckillStatEnum.END);
			return new SeckillResult<SeckillExecution>(false, seckillExecution);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			SeckillExecution seckillExecution = new SeckillExecution(seckillId,
					SeckillStatEnum.INNER_ERROR);
			return new SeckillResult<SeckillExecution>(false, seckillExecution);
		}

	}

	// 获取系统时间
	@RequestMapping(value = "time/now", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public SeckillResult<Long> time() {
		Date now = new Date();
		return new SeckillResult<Long>(true, now.getTime());
	}

}
