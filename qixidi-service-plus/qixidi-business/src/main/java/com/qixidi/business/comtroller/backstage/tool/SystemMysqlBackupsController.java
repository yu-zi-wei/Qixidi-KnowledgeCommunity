package com.qixidi.business.comtroller.backstage.tool;

import com.light.core.core.domain.R;
import com.light.core.utils.Constants;
import com.light.exception.ServiceException;
import com.qixidi.business.domain.entity.SystemMysqlBackups;
import com.qixidi.business.service.tool.SystemMysqlBackupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * mySQL数据备份
 */
@RequestMapping(value = "/api/system/baskups")
public class SystemMysqlBackupsController {

    /**
     * 数据库用户名
     */
    @Value("${spring.datasource.username}")
    private String userName;

    /**
     * 数据库密码
     */
    @Value("${spring.datasource.password}")
    private String password;

    /**
     * 数据库url
     */
    @Value("${spring.datasource.url}")
    private String url;

    /**
     * Windows数据库备份地址
     */
    @Value("${spring.datasource.win-path}")
    private String windowsPath;

    /**
     * Linux数据库备份地址
     */
    @Value("${spring.datasource.linux-path}")
    private String linuxPath;

    @Autowired
    private SystemMysqlBackupsService systemMysqlBackupsService;

    /**
     * 获取所有备份数据列表
     *
     * @return
     */
    @GetMapping("/backupsList")
    public R backupsList() {
        List<SystemMysqlBackups> systemMysqlBackups = systemMysqlBackupsService.selectBackupsList();
        return R.ok(systemMysqlBackups);
    }

    /**
     * MySQL备份
     *
     * @return
     */
    @PostMapping("/mysql_Backups")
    public R mysqlBackupsTwo() {
        String url = "jdbc:mysql://localhost:3306/aurora_admin";
        String user = "root";
        String password = "123";
        String backupPath = "E:\\zsh\\Desktop\\mysql";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             OutputStream outputStream = new FileOutputStream(backupPath)) {
            // 执行备份命令
//            String command = "mysqldump -u " + user + " -p" + password + " mydatabase";
            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec("mysqldump -hlocalhost -uroot -p123 aurora_admin  > E:\\zsh\\Desktop\\mysqlaurora_admin.sql");

            // 将备份数据写入输出流
            byte[] buffer = new byte[1024];
            int length;
            while ((length = process.getInputStream().read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            System.out.println("Backup successful.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return R.ok();
    }

    /**
     * MySQL备份
     *
     * @return
     */
    @PostMapping("/mysqlBackups")
    public R mysqlBackups() {
        String path = null;
        // 获取操作系统名称
        String osName = System.getProperty("os.name").toLowerCase();
        if (Constants.isSystem(osName)) {
            // Windows
            path = this.windowsPath;
        } else {
            // Linux
            path = this.linuxPath;
        }
        // 数据库用户名
        String userName = this.userName;
        // 数据库密码
        String password = this.password;
        // 数据库地址
        String url = this.url;
        // 调用备份
        Object systemMysqlBackups = systemMysqlBackupsService.mysqlBackups();
        return R.ok(systemMysqlBackups);
    }

    /**
     * 恢复数据库
     * @param map
     * @return
     */
    @PutMapping("/rollback")
    public R rollback(@RequestBody Map<String, Object> map) {
        Long id = Long.valueOf(map.get("id").toString());
        if (id == null) {
            throw new ServiceException("id不能为null，请重新尝试！");
        }
        // 数据库用户名
        String userName = this.userName;
        // 数据库密码
        String password = this.password;
        // 根据id查询查询已有的信息
        SystemMysqlBackups smb = systemMysqlBackupsService.selectListId(id);
        // 恢复数据库
        Object rollback = systemMysqlBackupsService.rollback(smb, userName, password);
        // 更新操作次数
        systemMysqlBackupsService.updateById(smb);
        return R.ok(rollback);
    }
}
