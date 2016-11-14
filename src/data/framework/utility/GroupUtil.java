package data.framework.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class GroupUtil {
	/**
     * 分組依據接口，用于集合分組時，獲取分組依據
     * @author  tang
     * @title   GroupUtil
     * @date    2015-5-15
     */
    public interface GroupBy<T> {
        T groupby(Object obj) ;
    }   
     
  /**
   * 
   * @param <T>
   * @param <D>
   * @param colls  排序集合
   * @param gb  分组字段
   * @return
   */
    public static final <T extends Comparable<T> ,D> Map<T ,List<D>> group(Collection<D> colls ,GroupBy<T> gb){
        if(colls == null || colls.isEmpty()) {
            System.out.println("分組集合不能為空!");
            return null ;
        }
        if(gb == null) {
            System.out.println("分組依據接口不能為Null!");
            return null ;
        }
        Iterator<D> iter = colls.iterator() ;
        Map<T ,List<D>> map = new HashMap<T, List<D>>() ;
        while(iter.hasNext()) {
            D d = iter.next() ;
            T t = gb.groupby(d) ;
            if(map.containsKey(t)) {
                map.get(t).add(d) ;
            } else {
                List<D> list = new ArrayList<D>() ;
                list.add(d) ;
                map.put(t, list) ;
            }
        }
        return map ;
    }   
     
     

}
