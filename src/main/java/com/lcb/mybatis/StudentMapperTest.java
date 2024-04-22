package com.lcb.mybatis;

import com.lcb.beans.Student;
import com.lcb.mapper.StudentMapper;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.List;

public class StudentMapperTest {

  private static SqlSessionFactory sqlSessionFactory;

  static {
    Reader reader = null;
    try {
      // 通过类加载器对配置文件进行加载，获取文件对应的字节流。
      reader = Resources.getResourceAsReader("mybatis-config.xml");

      // 解析mybatis配置文件，把配置文件转换为Configuration对象然后将其作为DefaultSqlSessionFactory类的构造函数参数，创建DefaultSqlSessionFactory对象
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }


  public static void main(String[] args) {
    // 打开一个session对象
    SqlSession sqlSession = null;
    try {
      /**
       * 创建事务对象、执行器对象、添加拦截器插件
       * @see DefaultSqlSessionFactory#openSession()
       */
      sqlSession = sqlSessionFactory.openSession();

      /**
       * 方式1（基于Statement ID）
       * @see DefaultSqlSession#selectList(String)
       */
      List<Object> objects = sqlSession.selectList("com.lcb.mapper.StudentMapper.selectAll");
      System.out.println(objects);

      /**
       * 方式2(基于Mapper接口，本质还是基于"方式1"的底层实现方法)
       * 通过session对象来获取mapper
       * @see DefaultSqlSession#getMapper(Class)
       */
      StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
      /**
       * @see MapperProxy#invoke(Object, Method, Object[])
       */
      List<Student> studentList = studentMapper.selectAll();
      System.out.println(studentList);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 记得关闭session
      if (sqlSession != null) {
        try {
          sqlSession.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
}
