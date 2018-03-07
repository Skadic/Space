package sebet.space.listener;

import android.util.SparseArray;

import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

/**
 * Created by eti22 on 02.08.2017.
 */

public class OnBarcodeReceivedListener implements BarcodeRetriever {
    @Override
    public void onRetrieved(Barcode barcode) {

    }

    @Override
    public void onRetrievedMultiple(Barcode barcode, List<BarcodeGraphic> list) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onRetrievedFailed(String s) {

    }
}
