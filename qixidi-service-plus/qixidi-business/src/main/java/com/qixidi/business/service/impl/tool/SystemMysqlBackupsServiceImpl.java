package com.qixidi.business.service.impl.tool;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.qixidi.business.domain.entity.SystemMysqlBackups;
import com.qixidi.business.mapper.tool.SystemMysqlBackupsMapper;
import com.qixidi.business.service.tool.SystemMysqlBackupsService;
import com.light.exception.ServiceException;
import com.light.core.utils.Constants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Software：IntelliJ IDEA 2021.2 x64
 * Date: 2021/9/16 15:19
 * ClassName:SystemMysqlBackupsService
 * 类描述： MySQL备份实现
 */
@Slf4j
@Service
public class SystemMysqlBackupsServiceImpl extends ServiceImpl<SystemMysqlBackupsMapper, SystemMysqlBackups> implements SystemMysqlBackupsService {

    @Resource
    private SystemMysqlBackupsMapper systemMysqlBackupsMapper;

    @Override
    public List<SystemMysqlBackups> selectBackupsList() {
        return systemMysqlBackupsMapper.selectBackupsList();
    }

    public static void main(String[] args) {
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
    }

    @Override
    public Object mysqlBackups() {
        String url = "jdbc:mysql://localhost:3306/aurora_admin";
        String userName = "root";
        String password = "123";
        String filePath = "E:\\zsh\\Desktop\\mysql";
        // 获取ip
        final String ip = url.substring(13, 22);
        // 获取端口号
        final String port = url.substring(23, 27);
        // 获取数据库名称
//        final String database_name = url.substring(28, 42);
        final String database_name = "aurora_admin";
        // 数据库文件名称
        StringBuilder mysqlFileName = new StringBuilder()
            .append(Constants.DATA_BASE_NAME)
            .append("_")
            .append(DateUtil.format(new Date(), "yyyy-MM-dd-HH-mm-ss"))
            .append(Constants.FILE_SUFFIX);
        // 备份命令
        StringBuilder cmd = new StringBuilder()
            .append("mysqldump ")
//            .append("--no-tablespaces ")
            .append("-h")
            .append(ip)
            .append(" -u")
            .append(userName)
            .append(" -p")
            .append(password)
            // 排除MySQL备份表
//            .append(" --ignore-table ")
            .append(database_name)
//            .append(".mysql_backups ")
//            .append(database_name)
            .append(" > ")
            .append(filePath)
            .append(mysqlFileName);
        // 判断文件是否保存成功
        if (!FileUtil.exist(filePath)) {
            FileUtil.mkdir(filePath);
//            return new th(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE.value(), "备份失败，文件保存异常，请查看文件内容后重新尝试！");
        }
        // 获取操作系统名称
        String osName = System.getProperty("os.name").toLowerCase();
        String[] command = new String[0];
        if (Constants.isSystem(osName)) {
            // Windows
//            command = new String[]{"cmd", "/c", String.valueOf(cmd)};
            command = new String[]{"cmd /c E:\\EnvironmentalScience\\mysql\\bin", String.valueOf(cmd)};
        } else {
            // Linux
            command = new String[]{"/bin/sh", "-c", String.valueOf(cmd)};
        }
        log.error("完整命令为：{}", command);
        SystemMysqlBackups smb = new SystemMysqlBackups();
        // 备份信息存放到数据库
        smb.setMysqlIp(ip);
        smb.setMysqlPort(port);
        smb.setBackupsName(String.valueOf(mysqlFileName));
        smb.setDatabaseName(database_name);
        smb.setMysqlCmd(String.valueOf(cmd));
        smb.setBackupsPath(filePath);
        smb.setCreateTime(DateTime.now());
        smb.setStatus(1);
        smb.setOperation(0);
        systemMysqlBackupsMapper.insert(smb);
        log.error("数据库备份命令为：{}", cmd);
        // 获取Runtime实例
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            if (process.waitFor() == 0) {
                log.info("Mysql 数据库备份成功,备份文件名：{}", mysqlFileName);
            } else {
                return new ServiceException("网络异常，数据库备份失败", HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("网络异常，数据库备份失败", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return smb;
    }

    //    @Override
    public SystemMysqlBackups selectListId(Long id) {
        return systemMysqlBackupsMapper.selectListId(id);
    }

    //    @Override
    public Object rollback(SystemMysqlBackups smb, String userName, String password) {
        // 备份路径和文件名
        StringBuilder realFilePath = new StringBuilder().append(smb.getBackupsPath()).append(smb.getBackupsName());
        if (!FileUtil.exist(String.valueOf(realFilePath))) {
//            return new ErrorTip(HttpStatus.NOT_FOUND.value(), "文件不存在，恢复失败，请查看目录内文件是否存在后重新尝试！");
        }
        StringBuilder cmd = new StringBuilder()
            .append("mysql -h")
            .append(smb.getMysqlIp())
            .append(" -u")
            .append(userName)
            .append(" -p")
            .append(password)
            .append(" ")
            .append(smb.getDatabaseName())
            .append(" < ")
            .append(realFilePath);
        String[] command = new String[0];
        log.error("数据库恢复命令为：{}", cmd);
        // 获取操作系统名称
        String osName = System.getProperty("os.name").toLowerCase();
        if (Constants.isSystem(osName)) {
            // Windows
            command = new String[]{"cmd", "/c", String.valueOf(cmd)};
        } else {
            // Linux
            command = new String[]{"/bin/sh", "-c", String.valueOf(cmd)};
        }
        // 恢复指令写入到数据库
        smb.setMysqlBackCmd(String.valueOf(cmd));
        // 更新操作次数
        smb.setRecoveryTime(DateTime.now());
        smb.setOperation(smb.getOperation() + 1);
        // 获取Runtime实例
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            if (process.waitFor() == 0) {
                log.error("Mysql 数据库恢复成功,恢复文件名：{}", realFilePath);
            } else {
//                return new ErrorTip(HttpStatus.GATEWAY_TIMEOUT.value(), "网络异常，恢复失败，请稍后重新尝试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
//            return new ErrorTip(HttpStatus.GATEWAY_TIMEOUT.value(), "网络异常，恢复失败，请稍后重新尝试！");
        }
        return smb;
    }
}
