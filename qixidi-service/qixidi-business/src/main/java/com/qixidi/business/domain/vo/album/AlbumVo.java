package com.qixidi.business.domain.vo.album;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import com.qixidi.business.domain.entity.album.Album;

/**
 * 相册图片
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class AlbumVo extends Album {

    /**
     * 归属用户昵称（列表展示用）
     */
    private String nickname;

    /**
     * 归属用户头像（列表展示用）
     */
    private String avatar;
}
