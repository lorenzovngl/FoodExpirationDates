package com.lorenzovainigli.foodexpirationdates.view.composable

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.OpenFoodFactsJsonResponse
import com.lorenzovainigli.foodexpirationdates.model.Product
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.BarcodeScannerActivity
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.EXTRA_PRODUCT_NAME
import com.lorenzovainigli.foodexpirationdates.viewmodel.APIServiceViewModel.*

@Composable
fun BarcodeScannerResult(
    activity: BarcodeScannerActivity?,
    state: BarcodeScannerState,
    responseOk: Boolean? = null,
    detectedProduct: OpenFoodFactsJsonResponse? = null
) {
    val apiServiceViewModel = activity?.apiServiceViewModel
    val height = 100.dp
    var error = false
    Column {
        Row(
            modifier = Modifier
                .height(height)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            when (state) {
                BarcodeScannerState.READY_TO_SCAN ->
                    BarcodeScannerResultReadyToScan(height = height)

                BarcodeScannerState.GETTING_PRODUCT_INFO ->
                    BarcodeScannerResultGettingInfo()

                BarcodeScannerState.GOT_RESULT ->
                    if (responseOk != null && !responseOk) {
                        BarcodeScannerResultResponseError(
                            height = height,
                            message = stringResource(id = R.string.connection_error)
                        )
                        error = true
                    } else if (detectedProduct != null) {
                        if (detectedProduct.status == 1) {
                            BarcodeScannerResultGotResult(
                                height = height,
                                detectedProduct = detectedProduct
                            )
                        } else {
                            BarcodeScannerResultResponseError(
                                height = height,
                                message = stringResource(R.string.product_not_found)
                            )
                            error = true
                        }
                    }

                BarcodeScannerState.SCANNING_ERROR -> {
                    BarcodeScannerResultResponseError(
                        height = height,
                        message = stringResource(R.string.scanning_error)
                    )
                    error = true
                }

                BarcodeScannerState.NO_CONNECTION -> {
                    BarcodeScannerResultResponseError(
                        height = height,
                        icon = Icons.Outlined.WifiOff,
                        message = stringResource(R.string.no_internet_connection)
                    )
                    error = true
                }
            }
        }
        Row {
            OutlinedButton(
                onClick = { activity?.finish() },
                modifier = Modifier
                    .weight(0.5f)
                    .padding(8.dp),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.primary
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
            ) {
                Text(text = stringResource(id = R.string.back))
            }
            if (state != BarcodeScannerState.NO_CONNECTION) {
                if (error) {
                    Button(
                        onClick = {
                            apiServiceViewModel?.setBarcodeScannerState(BarcodeScannerState.READY_TO_SCAN)
                        },
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                    ) {
                        Text(text = stringResource(id = R.string.retry))
                    }
                } else {
                    Button(
                        onClick = {
                            activity?.let {
                                val data = Intent().apply {
                                    putExtra(EXTRA_PRODUCT_NAME, detectedProduct?.product?.productName)
                                }
                                it.setResult(Activity.RESULT_OK, data)
                                it.finish()
                            }
                        },
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(8.dp),
                        enabled = detectedProduct?.product?.productName?.isNotEmpty() ?: false
                    ) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                }
            }
        }
    }
}


@Composable
fun BarcodeScannerResultGettingInfo() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Text(
            text = stringResource(R.string.getting_product_info),
            color = Color.Gray
        )
    }
}

@Composable
fun BarcodeScannerResultReadyToScan(height: Dp) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(height / 2),
            painter = painterResource(id = R.drawable.ic_barcode_scan),
            contentDescription = null,
            tint = Color.Gray
        )
        Text(
            text = stringResource(R.string.please_scan_a_barcode),
            color = Color.Gray
        )
    }
}

@Composable
fun BarcodeScannerResultGotResult(height: Dp, detectedProduct: OpenFoodFactsJsonResponse) {
    AsyncImage(
        modifier = Modifier
            .size(height)
            .padding(8.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        model = ImageRequest.Builder(LocalContext.current)
            .data(detectedProduct.product.imageThumbUrl)
            .crossfade(true)
            .build(),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        placeholder = painterResource(id = R.drawable.icons8_cesto_96),
        onError = {
            Log.e("Coil", "Error: ${it.result.throwable.message}")
            it.result.throwable.printStackTrace()
        }
    )
    Column(
        modifier = Modifier.padding(start = 8.dp, end = height / 2)
    ) {
        Text(
            text = detectedProduct.product.brands,
            fontSize = 18.sp,
            color = Color.Gray
        )
        Text(
            text = detectedProduct.product.productName,
            fontSize = 24.sp,
        )
    }
}

@Composable
fun BarcodeScannerResultResponseError(
    height: Dp,
    icon: ImageVector = Icons.Outlined.ErrorOutline,
    message: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(height / 2),
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@PreviewLightDark
@Composable
fun BarcodeScannerResultReadyToScanPreview() {
    FoodExpirationDatesTheme {
        Surface {
            BarcodeScannerResult(
                activity = null,
                state = BarcodeScannerState.READY_TO_SCAN,
            )
        }
    }
}

@PreviewLightDark
@Composable
fun BarcodeScannerResultGettingInfoPreview() {
    FoodExpirationDatesTheme {
        Surface {
            BarcodeScannerResult(
                activity = null,
                state = BarcodeScannerState.GETTING_PRODUCT_INFO
            )
        }
    }
}

@PreviewLightDark
@Composable
fun BarcodeScannerResultPreview() {
    FoodExpirationDatesTheme {
        Surface {
            val detectedProduct = OpenFoodFactsJsonResponse(
                code = "",
                product = Product(
                    brands = "Ferrero",
                    productName = "Nutella",
                    imageThumbUrl = "https://images.openfoodfacts.org/images/products/301/762/042/5035/front_de.474.100.jpg"
                ),
                status = 1
            )
            BarcodeScannerResult(
                activity = null,
                state = BarcodeScannerState.GOT_RESULT,
                responseOk = true,
                detectedProduct = detectedProduct
            )
        }
    }
}

@PreviewLightDark
@Composable
fun BarcodeScannerResultProductNotFoundPreview() {
    FoodExpirationDatesTheme {
        Surface {
            val detectedProduct = OpenFoodFactsJsonResponse(
                code = "",
                product = Product(
                    brands = "Ferrero",
                    productName = "Nutella",
                    imageThumbUrl = "https://images.openfoodfacts.org/images/products/301/762/042/5035/front_de.474.100.jpg"
                ),
                status = 0
            )
            BarcodeScannerResult(
                activity = null,
                state = BarcodeScannerState.GOT_RESULT,
                responseOk = true,
                detectedProduct = detectedProduct
            )
        }
    }
}

@PreviewLightDark
@Composable
fun BarcodeScannerResultResponseErrorPreview() {
    FoodExpirationDatesTheme {
        Surface {
            BarcodeScannerResult(
                activity = null,
                state = BarcodeScannerState.GOT_RESULT,
                responseOk = false
            )
        }
    }
}

@PreviewLightDark
@Composable
fun BarcodeScannerResultNoInternetConnectionPreview() {
    FoodExpirationDatesTheme {
        Surface {
            BarcodeScannerResult(
                activity = null,
                state = BarcodeScannerState.NO_CONNECTION,
                responseOk = false
            )
        }
    }
}
