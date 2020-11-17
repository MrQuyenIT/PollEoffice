package quyennv.becamex.voteeoffice;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tylersuehr.chips.Chip;
import com.tylersuehr.chips.ChipImageRenderer;
import com.tylersuehr.chips.LetterTileProvider;

public class GlideRenderer implements ChipImageRenderer {
    @Override
    public void renderAvatar(ImageView imageView, Chip chip) {
        if (chip.getAvatarUri() != null) {
            Glide.with(imageView.getContext())
                    .load(chip.getAvatarUri())
                    .into(imageView);
        } else {
            imageView.setImageBitmap(LetterTileProvider
                    .getInstance(imageView.getContext())
                    .getLetterTile(chip.getTitle()));
        }
    }
}