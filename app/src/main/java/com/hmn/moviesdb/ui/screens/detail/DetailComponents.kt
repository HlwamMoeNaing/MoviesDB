package com.hmn.moviesdb.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hmn.moviesdb.R


@Composable
fun DetailAppBar(
    tittle:String,
    isShowFavButton:Boolean = false,
    isFav:Boolean = false,
    onBackClick:()->Unit,
    onFavClickL:()->Unit,
    tintColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            onBackClick()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                // tint = primaryPink,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }
        Text(
            modifier = Modifier.width(200.dp),
            text = tittle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.SemiBold
        )

        if (isShowFavButton){
            IconButton(onClick = {
                onFavClickL()

            }) {
                Icon(
                    painter = painterResource(id = R.drawable.fav_filled),

                    tint = if (isFav) {
                        Color.Red
                    } else {
                        Color.LightGray
                    },
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        }


    }
}
