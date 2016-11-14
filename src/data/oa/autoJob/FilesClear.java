package data.oa.autoJob;

import java.io.File;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import data.framework.support.Logger;
import data.platform.entity.EntityPlatformAccessory;
import data.platform.service.PlatformAccessoryService;

/**
 * 自动任务: 删除文件表 无关联的文件、并删除物理文件
 * @author Administrator
 *
 */
public class FilesClear {
	
	private Logger logger = Logger.getLogger( this.getClass() ) ;
	
	@Autowired
	private PlatformAccessoryService platformAccessoryService;
	
	public void run() {
		
		logger.info("===============FilesClear job start...==============");
		
		/*List<EntityPlatformAccessory> list = platformAccessoryService.loadNullAssociated();
		
		if(list != null && list.size() > 0) {
			
			for (EntityPlatformAccessory entity : list) {
				
				String id = entity.getId();
				String filePath = entity.getFilePathInServer();
				
				if(StringUtils.isNotEmpty(id))
					this.platformAccessoryService.remove(id);
				
				if(StringUtils.isNotEmpty(filePath)) {
					File file = new File(filePath) ;
			        if( file.exists() ){
			            file.delete() ;
			        }
				}
				
			}
		}*/
		
		logger.info("===============FilesClear job end...==============");
	}

}
